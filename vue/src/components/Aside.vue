<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">

      <el-menu-item index="/data">
        <i class="el-icon-menu"></i>
        <template #title>数据管理</template>
      </el-menu-item>
<!--      <el-menu-item index="user">
        <i class="el-icon-setting"></i>
        <template #title>用户管理</template>
      </el-menu-item>-->
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      path: this.$route.path
    }
  },
  created() {
  }
}
</script>

<style scoped>

</style>