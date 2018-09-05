/***
 * @pName management
 * @name VersionResp
 * @user HongWei
 * @date 2018/9/5
 * @desc
 */
package com.panda.game.management.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VersionResp {
    private Integer update;
    private String version;
}
