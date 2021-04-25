package com.lishan.spark.core.framework.common

import com.lishan.spark.core.framework.util.EnvUtil


trait TDao {

    def readFile(path:String) = {
        EnvUtil.take().textFile(path)
    }
}
