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

@Table(name = "tb_permission_relation")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRelation {
    @Id
    private Integer permissionRelationId;
    private Integer permissionId;
    private Integer userId;
    private String permissionRole;
}
