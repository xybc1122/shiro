package com.shiro.demoshiro.doman;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class User implements Serializable{
    private Integer uid;

    private String userName;//帐号

    private Date createdTime; //创建时间

    private Date birth; //生日

    private String name;//名称（昵称或者真实姓名，不同系统不同定义）

    private String passWord; //密码;

    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

    private Set<URole> roles; //角色对象
    /**
     * 表关联--@JoinTable
     * name属性为连接两个表的表名称。若不指定，则使用默认的表名称，
     * joinColumn属性表示，在保存关系的表中，所保存关联关系的外键的字段，并配合@JoinColumn标记使用；
     * inverseJoinColumn属性与joinColumn类似，它保存的是保存关系的另外一个外键字段；
     */
   /* @ManyToMany(fetch = FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<SysRole> roleList;// 一个用户具有多个角色*/
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Set<URole> getRoles() {
        return roles;
    }

    public void setRoles(Set<URole> roles) {
        this.roles = roles;
    }

    public User() {

    }

}


