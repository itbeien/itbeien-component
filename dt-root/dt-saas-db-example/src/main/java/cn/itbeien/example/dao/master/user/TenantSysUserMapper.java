package cn.itbeien.example.dao.master.user;

import cn.itbeien.example.entity.SysUser;
import cn.itbeien.example.entity.SysUserExample;
import cn.itbeien.root.ds.component.DataSource;
import cn.itbeien.root.ds.constant.DbReadWriteModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@DataSource(DbReadWriteModel.READ_WRITE)
public interface TenantSysUserMapper {
    long countByExample(SysUserExample example);

    int deleteByExample(SysUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysUser row);

    int insertSelective(SysUser row);

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(@Param("id")Long id,@Param("tenantCode") String tenantCode);

    int updateByExampleSelective(@Param("row") SysUser row, @Param("example") SysUserExample example);

    int updateByExample(@Param("row") SysUser row, @Param("example") SysUserExample example);

    int updateByPrimaryKeySelective(SysUser row);

    int updateByPrimaryKey(SysUser row);
}