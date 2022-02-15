package com.rainsoil.common.framework.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.rainsoil.common.framework.xss.utils.XssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * jackson xss处理
 *
 * @author luyanan
 * @since 2021/8/17
 **/
@Slf4j
public class JacksonXssClean extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {

		String text = jsonParser.getValueAsString();
		if (null == text) {
			return null;
		}
		if (XssHolder.isEnabled()) {
			String value = XssUtil.clean(text);
			log.trace("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);

			return value;
		}
		else {
			return text;
		}
	}

}
