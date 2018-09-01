/***
 * @pName management
 * @name Permission
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_permission")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    private Integer permissionId;
    private String permissionName;
    private String targetUrl;
}
