<template>
  <div class="app-container">
    <div class="empty-svg">
      <svg-icon icon-class="empty" />
    </div>
    <div class="empty-content">
      <a href="" class="empty-button">Go To Create New</a>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.app-container {
  transform: translate(-50%,-50%);
  position: absolute;
  top: 20%;
  left: 50%;
}

.empty-svg {
  font-size: 300px;
}

.empty-content {
  transform: translate(-50%,-50%);
  position: absolute;
  left: 50%;
}

.empty-button {
  display: block;
  float: left;
  width: 150px;
  height: 36px;
  background: #1482f0;
  border-radius: 100px;
  text-align: center;
  color: #ffffff;
  font-size: 14px;
  line-height: 36px;
  cursor: pointer;
}
</style>
