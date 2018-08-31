/***
 * @pName management
 * @name DynamicConfiguration
 * @user HongWei
 * @date 2018/8/31
 * @desc
 */
package com.panda.game.management.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="app")
@Getter
@Setter
public class DynamicConfiguration {
    private String domain;
}
