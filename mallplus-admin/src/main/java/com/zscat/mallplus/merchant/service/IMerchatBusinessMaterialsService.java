package com.zscat.mallplus.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.merchant.entity.MerchatBusinessMaterials;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Map;

/**
* @author mallplus
* @date 2020-05-14
*/

public interface IMerchatBusinessMaterialsService extends IService<MerchatBusinessMaterials> {

    public String queryMax(String tableName, String columnName);

    Map<String, Object> getBody(MerchatBusinessMaterials merchatBusinessMaterials,String certPath) throws IOException, IllegalBlockSizeException;

    Map<String,Object> validInfo(MerchatBusinessMaterials merchatBusinessMaterials);
}
