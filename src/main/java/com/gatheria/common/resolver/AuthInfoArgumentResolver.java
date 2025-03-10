package com.gatheria.common.resolver;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(Auth.class) != null
        && parameter.getParameterType().equals(AuthInfo.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return webRequest.getAttribute("authInfo", RequestAttributes.SCOPE_REQUEST);
  }
}
