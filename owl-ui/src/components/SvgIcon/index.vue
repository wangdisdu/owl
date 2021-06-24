<template>
  <div v-if="isUrl" :style="styleUrlIcon" class="svg-url-icon svg-icon" v-on="$listeners" />
  <svg v-else :class="svgClass" aria-hidden="true" v-on="$listeners">
    <use :xlink:href="iconName" />
  </svg>
</template>

<script>

export default {
  name: 'SvgIcon',
  props: {
    iconClass: {
      type: String,
      required: true
    },
    iconUrl: {
      type: String,
      default: ''
    },
    className: {
      type: String,
      default: ''
    }
  },
  computed: {
    isUrl() {
      if (this.iconUrl) {
        return true
      } else {
        return false
      }
    },
    iconName() {
      return `#icon-${this.iconClass}`
    },
    svgClass() {
      if (this.className) {
        return 'svg-icon ' + this.className
      } else {
        return 'svg-icon'
      }
    },
    styleUrlIcon() {
      return {
        mask: `url(${this.iconUrl}) no-repeat 50% 50%`,
        '-webkit-mask': `url(${this.iconUrl}) no-repeat 50% 50%`
      }
    }
  }
}
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.2em;
  fill: currentColor;
  overflow: hidden;
}

.svg-url-icon {
  background-color: currentColor;
  mask-size: cover!important;
  display: inline-block;
}
</style>
