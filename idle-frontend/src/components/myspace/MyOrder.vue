<template>
  <el-card>
      <el-row :gutter="20">
          <el-col :span="8">
              <el-input placeholder="请输入订单编号" v-model="form.search">
                  <template #suffix>
                      <el-icon style="cursor: pointer" @click="searchOrder"><Search /></el-icon>
                  </template>
              </el-input>
          </el-col>
      </el-row>

      <el-table :data="form.orderList" stripe>
          <el-table-column label="订单编号" width="110" align="center" sortable prop="id"></el-table-column>
          <el-table-column label="商品名称" align="center" prop="product_name"></el-table-column>
          <el-table-column label="订单价格(元)" width="130" align="center" sortable prop="price"></el-table-column>
          <el-table-column label="卖家" width="80" align="center" prop="seller"></el-table-column>
          <el-table-column label="买家" width="80" align="center" prop="buyer"></el-table-column>
          <el-table-column label="支付方式" width="110" align="center" sortable prop="payment"></el-table-column>
          <el-table-column label="订单状态" width="110" align="center" sortable prop="status">
              <template #default="scope">
                  <div v-if="scope.row.status !== 0">
                      <el-tag effect="dark" type="success">
                          已完成
                      </el-tag>
                  </div>
                  <div v-else>
                      <el-tag effect="dark" type="danger">
                          未完成
                      </el-tag>
                  </div>
              </template>
          </el-table-column>
          <el-table-column label="下单时间" width="120" align="center" sortable prop="create_time"></el-table-column>
          <el-table-column label="收货地址" width="200" align="center" prop="shipping_address"></el-table-column>
          <el-table-column label="操作" align="center" width="70">
              <template #default="scope">
                  <el-tooltip content="确认订单" placement="top">
                      <el-icon @click="handleCheck(scope.row.status,scope.row.id)" style="cursor:pointer;margin-right: 10px">
                          <Check/>
                      </el-icon>
                  </el-tooltip>
                <el-tooltip content="取消订单" placement="top">
                    <el-icon @click="handleCancel(scope.row.id)" style="cursor: pointer">
                        <Delete/>
                    </el-icon>
                </el-tooltip>
              </template>
          </el-table-column>
      </el-table>
  </el-card>
</template>

<script setup>
import {reactive, onMounted} from 'vue'
import {post} from "@/request/request";
import {useRoute} from 'vue-router'
import {Search, Delete, Check} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'

const route = useRoute()
const name = route.query.nickname
const form = reactive({
    orderList:[],
    search: ''
})
const getOwnOrder = () => {
    post('/api/orders/get-own-order',{
        nickname: name
    },message => {
        form.orderList=message
    })
}

onMounted(async () => {
    await getOwnOrder()
})

const searchOrder = () => {
    post('/api/orders/get-id-order',{
        id: form.search,
        nickname: name
    },message =>{
        ElMessage.success("搜索成功")
        form.orderList = message
    })
}

const handleCheck = (status,id) => {
    if(status === 1){
        ElMessage.warning("订单已完成，请不要重复操作")
    } else {
        post('/api/orders/check-order',{
            id: id
        }, message => {
            ElMessage.success(message)
        })
    }
}

const handleCancel = (id) => {
    post('/api/orders/delete-order',{
        id: id
    }, message =>{
        ElMessage.success(message)
    })
}
</script>