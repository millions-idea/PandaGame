/***
 * @pName GameManagement
 * @name RoomMembersResp
 * @user HongWei
 * @date 2018/10/13
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
public class RoomMembersResp {
    private Integer count;
    private Integer persons;
}
