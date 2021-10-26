<template>
  <div style="padding: 10px">

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 25%;" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="primary" @click="uploadForm">上传</el-button>
    </div>

    <el-table
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="ID"
          sortable>
      </el-table-column>
      <el-table-column
          prop="rpath"
          label="轨道号">
      </el-table-column>
      <el-table-column
          prop="rrow"
          label="行编号">
      </el-table-column>
      <el-table-column
          prop="rdesc"
          label="空间投影以及坐标系等">
      </el-table-column>
      <el-table-column
          prop="sensorType"
          label="传感器类型">
      </el-table-column>
      <el-table-column
          prop="rsatelite"
          label="卫星类型">
      </el-table-column>
      <el-table-column
          prop="rcollectDate"
          label="采集时间"
          sortable>
      </el-table-column>
      <el-table-column
          label="几何中心点坐标">
        <template #default="scope">
          ({{scope.row.centerLong}},{{scope.row.centerLat}})
        </template>
      </el-table-column>
      <el-table-column
          prop="productId"
          label="数据类型">
      </el-table-column>
      <el-table-column
          prop="band"
          label="影像的波段数">
      </el-table-column>
      <el-table-column
          label="左上点坐标">
        <template #default="scope">
          ({{scope.row.upperLeftLong}},{{scope.row.upperLeftLat}})
        </template>
      </el-table-column>
      <el-table-column
          label="右上点坐标">
        <template #default="scope">
          ({{scope.row.upperRightLong}},{{scope.row.upperRightLat}})
        </template>
      </el-table-column>
      <el-table-column
          label="左下点坐标">
        <template #default="scope">
          ({{scope.row.lowerLeftLong}},{{scope.row.lowerLeftLat}})
        </template>
      </el-table-column>
      <el-table-column
          label="右下点坐标">
        <template #default="scope">
          ({{scope.row.lowerRightLong}},{{scope.row.lowerRightLat}})
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-link type="primary" @click="downloadFile(scope.row.hdfsPath)">下载</el-link>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog
        title="上传文件"
        v-model="dialogVisible">

      <el-form ref="form" label-position="left" :model="form" :rules="rules" label-width="80px"  style="margin:20px;width:60%;min-width:600px;">

        <el-form-item label="坐标类型" prop="satelite">
          <el-radio-group v-model="form.satelite">
            <el-radio label="GF7-1">高分七号</el-radio>
            <el-radio label="ZY1-1">资源一号</el-radio>
            <el-radio label="ZY3-1">资源三号</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="上传文件" prop="zipFile">
          <el-upload
              class="upload-demo"
              ref="upload"
              :action="uploadUrl()"
              name="zipFile"
              drag
              :data="upData"
              :file-list="fileList"
              :on-error="uploadFalse"
              :on-success="uploadSuccess"
              :auto-upload="false"
              :before-upload="beforeAvatarUpload">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <!-- <el-button slot="trigger" size="small" >选取文件</el-button> -->
            <div slot="tip" class="el-upload__tip" style="color:red">上传文件只能是 xls、xlsx、txt 格式!</div>
          </el-upload>
        </el-form-item>

          <el-form-item>
          <el-button type="primary" @click="submitUpload(form)">导入</el-button>
          <el-button   @click="onCancel(form)">取消</el-button>
          </el-form-item>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>

import request from "../utils/request";

export default {
  name: 'Data',
  components: {

  },
  data() {
    return {
      rules: {
        satelite: [
          { required: true, message: '请选择zip文件中的卫星类型', trigger: 'change' }
        ],
      },
      form: {
        fileName:'',
        satelite: 'GF7-1',
      },
      fileList: [],

      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize:10,
      total: 0,
      tableData: [

      ],
      sateliteOptions: [
        {
          value: 'GF7-1',
          label: 'GF7-1',
        },
        {
          value: 'GF7-2',
          label: 'GF7-2',
        },
      ],
    }
  },
  computed: {
    // 这里定义上传文件时携带的参数，即表单数据
    upData: function() {
      return this.form
    }
  },
  created() {
    this.load()
  },
  methods: {
    //导入接口地址
    uploadUrl: function() {
      return '/api/data/hdfs/upload'  //接口
    },
    //文件上传成功触发
    uploadSuccess(response, file, fileList) {
      console.log(response)
      if(response.code == 200){
        this.$message({
          message: '上传成功',
          type: 'success'
        });
      }else {
        this.$message({
          message: '上传失败',
          type: 'error'
        });
      }
    },
    //文件上传失败触发
    uploadFalse(response, file, fileList) {
      this.$message({
        message: '文件上传失败！',
        type: 'error'
      });
    },
    // 上传前对文件的大小和类型的判断
    beforeAvatarUpload(file) {
      this.form.fileName = file.name;
      const extension = file.name.split(".")[1] === "xls";
      const extension2 = file.name.split(".")[1] === "xlsx";
      const extension3 = file.name.split(".")[1] === "txt";
      if (!extension && !extension2 && !extension3) {
        this.$message({
          message: '上传文件只能是 xls、xlsx、txt 格式!',
          type: 'error'
        });
      }
      return extension || extension2 || extension3 ;
    },
    //表单提交
    submitUpload(form) {
      this.$refs.form.validate((valid) => {
        if (valid) {
          //触发组件的action
          this.$refs.upload.submit();
          this.load()//刷新表格的数据
          this.dialogVisible = false //关闭弹窗

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //表单取消
    onCancel(form){
      this.$refs.form.resetFields();
      this.dialogVisible = false;
    },
    load() {
      request.get("/data", {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
        }

      }).then(res => {
        console.log(res)
        this.tableData = res.data.records,
            this.total = res.data.total
      })
    },
    //下载文件
    downloadFile(hdfsPath){
      let actionUrl = '/api/data/hdfs/downloadFile?fileName=' + hdfsPath;
      window.open(actionUrl);
    },
    uploadForm() {
      this.dialogVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(id) {
      console.log(id)
      request.delete("/data/" + id).then(res => {
        if (res.code === '200'){
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() //删除之后，重新加载表格的数据
      })
    },
    handleSizeChange(pageSize) { //改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { //改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
