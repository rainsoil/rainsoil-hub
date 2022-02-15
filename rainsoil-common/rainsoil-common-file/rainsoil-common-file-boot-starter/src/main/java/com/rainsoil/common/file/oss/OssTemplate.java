package com.rainsoil.common.file.oss;

import cn.hutool.core.date.DateUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.rainsoil.common.file.core.FileFilterArgs;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * aws-s3 通用存储操作 支持所有兼容s3协议的云存储: {阿里云OSS，腾讯云COS，七牛云，京东云，minio 等}
 *
 * @author luyanan
 * @since 2021/10/23
 **/
@RequiredArgsConstructor
public class OssTemplate implements InitializingBean {

	private final OssProperties ossProperties;

	private AmazonS3 amazonS3;

	/**
	 * 创建bucket
	 *
	 * @param bucketName bucket名称
	 * @return void
	 * @since 2021/10/23
	 */
	@SneakyThrows()
	public void createBucket(String bucketName) {

		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket(bucketName);
		}
	}


	/**
	 * 文件是否存在
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return void
	 * @since 2021/10/24
	 */
	public boolean existObject(String bucketName, String objectName) {
		return amazonS3.doesObjectExist(bucketName, objectName);
	}


	/**
	 * 获取全部的bucket
	 *
	 * @return java.util.List<com.amazonaws.services.s3.model.Bucket>
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS API Documentation</a>
	 * @since 2021/10/23
	 */
	@SneakyThrows()
	public List<Bucket> getAllBuckets() {
		return amazonS3.listBuckets();
	}

	/**
	 * 根据bucket名称获取bucket
	 *
	 * @param bucketName bucket名称
	 * @return java.util.Optional<com.amazonaws.services.s3.model.Bucket>
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS  API Documentation</a>
	 * @since 2021/10/23
	 */
	public Optional<Bucket> getBucket(String bucketName) {
		return amazonS3.listBuckets().stream().filter(a -> a.getName().equals(bucketName)).findFirst();
	}

	/**
	 * 根据bucket名称删除bucket
	 *
	 * @param bucketName bucket名称
	 * @return void
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API Documentation</a>
	 * @since 2021/10/23
	 */
	public void removeBucket(String bucketName) {
		amazonS3.deleteBucket(bucketName);
	}


	/**
	 * 根据文件前置查询文件
	 *
	 * @param bucketName     bucketName
	 * @param fileFilterArgs 文件过滤
	 * @return java.util.List<com.amazonaws.services.s3.model.S3ObjectSummary>
	 * @since 2021/10/23
	 */
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, FileFilterArgs fileFilterArgs) {

		ListObjectsV2Request request = new ListObjectsV2Request();
		request.setBucketName(bucketName);
		if (null != fileFilterArgs) {
			request.setPrefix(fileFilterArgs.getPrefix());
			request.setMaxKeys(fileFilterArgs.getSize());
			request.setStartAfter(fileFilterArgs.getStartAfter());
		}
		ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(request);
		return listObjectsV2Result.getObjectSummaries();
	}


	/**
	 * 获取文件外链
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires    失效时间<=7
	 * @return java.lang.String
	 * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
	 * @since 2021/10/24
	 */
	public String getObjectURL(String bucketName, String objectName, Integer expires) {

		Date expireDate = DateUtil.offsetDay(new Date(), expires).toJdkDate();
		URL url = amazonS3.generatePresignedUrl(bucketName, objectName, expireDate);
		return url.toString();
	}

	/**
	 * 获取文件
	 *
	 * @param bucketName bucket 名称
	 * @param objectName 文件名称
	 * @return com.amazonaws.services.s3.model.S3Object  二进制流
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS API Documentation</a>
	 * @since 2021/10/24
	 */
	public S3Object getObject(String bucketName, String objectName) {
		return amazonS3.getObject(bucketName, objectName);
	}

	/**
	 * 上传文件
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream     文件流
	 * @return void
	 * @since 2021/10/24
	 */
	@SneakyThrows(Exception.class)
	public void putObject(String bucketName, String objectName, InputStream stream) {
		putObject(bucketName, objectName, stream, (long) stream.available(), "application/octet-stream");
	}

	/**
	 * 上传文件
	 *
	 * @param bucketName  bucket 名称
	 * @param objectName  文件名称
	 * @param stream      io流
	 * @param size        大小
	 * @param contextType 文件类型
	 * @return com.amazonaws.services.s3.model.PutObjectResult
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS API Documentation</a>
	 * @since 2021/10/24
	 */
	public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
									 String contextType) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(size);
		objectMetadata.setContentType(contextType);
		return amazonS3.putObject(bucketName, objectName, stream, objectMetadata);
	}

	/**
	 * 获取文件信息
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return com.amazonaws.services.s3.model.S3Object
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS API Documentation</a>
	 * @since 2021/10/24
	 */
	@SneakyThrows
	public S3Object getObjectInfo(String bucketName, String objectName) {
		@Cleanup
		S3Object object = amazonS3.getObject(bucketName, objectName);
		return object;
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名
	 * @return void
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS APIDocumentation</a>
	 * @since 2021/10/24
	 */
	@SneakyThrows
	public void removeObject(String bucketName, String objectName) {
		amazonS3.deleteObject(bucketName, objectName);
	}

	@Override
	public void afterPropertiesSet() {

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(ossProperties.getMaxConnections());

		AwsClientBuilder.EndpointConfiguration endpointConfiguration =
				new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(), ossProperties.getRegion());

		AWSCredentials awsCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(), ossProperties.getSecretKey());

		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
		this.amazonS3 = AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
				.withClientConfiguration(clientConfiguration)
				.withCredentials(awsCredentialsProvider)
				.disableChunkedEncoding()
				.withPathStyleAccessEnabled(ossProperties.getPathStyleAccess())
				.build();

	}

	public AmazonS3 getClient() {
		return this.amazonS3;
	}
}
