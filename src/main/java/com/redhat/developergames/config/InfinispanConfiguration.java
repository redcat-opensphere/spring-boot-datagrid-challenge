package com.redhat.developergames.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.spring.starter.remote.InfinispanRemoteCacheCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class InfinispanConfiguration {

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   public InfinispanRemoteCacheCustomizer caches() {
      return b -> {
         // Configure the weather cache to be created if it does not exist in the first call
         URI weatherCacheConfigUri = cacheConfigURI("weatherCache.xml");

         b.remoteCache("weather")
                 .configurationURI(weatherCacheConfigUri);

			// Ask the server to create this cache on startup
			b.remoteCache("sessions").configuration(
					"<distributed-cache name=\"sessions\"><encoding media-type=\"application/x-protostream\"/></distributed-cache>");

			// Use protostream marshaller to serialize the sessions with Protobuf
			b.remoteCache("sessions").marshaller(ProtoStreamMarshaller.class);

      };
   }

   private URI cacheConfigURI(String cacheConfigFile) {
      URI cacheConfigUri;
      try {
         cacheConfigUri = this.getClass().getClassLoader().getResource(cacheConfigFile).toURI();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }
      return cacheConfigUri;
   }

}
