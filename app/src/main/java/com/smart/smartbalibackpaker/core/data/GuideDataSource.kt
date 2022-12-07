package com.smart.smartbalibackpaker.core.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.vo.Resource

interface GuideDataSource {

    fun insertNode(guide: GuideMapsEntity)

    fun getNodes() : LiveData<List<GuideMapsEntity>>

    fun deleteNode()

    fun deleteNodes()

}