/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string')) {
      if ((/^[0-9]+$/.test(time))) {
        // support "1548221490638"
        time = parseInt(time)
      } else {
        // support safari
        // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
        time = time.replace(new RegExp(/-/gm), '/')
      }
    }

    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    return value.toString().padStart(2, '0')
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}

export function humanReadable(value, unit) {
  if (unit === 'percent') {
    return value + '%'
  } else if (unit === 'count') {
    return humanCount(value)
  } else if (unit === 'ms') {
    return humanMs(value)
  } else if (unit === 'bytes') {
    return humanBytes(value)
  } else if (unit === 'kilobytes') {
    return humanKiloBytes(value)
  } else {
    return value
  }
}

/**
 * Format count as human-readable text.
 * @param bytes Number.
 * @returns Formatted string.
 */
export function humanCount(count) {
  if (Math.abs(count) < 10000) {
    return count + ''
  }
  const dp = 1
  const units = ['万', '亿', '万亿']
  let u = -1
  const r = 10 ** dp
  do {
    count /= 10000
    ++u
  } while (Math.round(Math.abs(count) * r) / r >= 10000 && u < units.length - 1)
  return count.toFixed(dp) + ' ' + units[u]
}

/**
 * Format milliseconds as human-readable text.
 * @param milliseconds.
 * @returns Formatted string.
 */
export function humanMs(ms) {
  if (Math.abs(ms) < 1000) {
    return ms + '毫秒'
  }
  ms = ms / 1000
  if (ms < 60) {
    return ms.toFixed(1) + '秒'
  }
  ms = ms / 60
  if (ms < 60) {
    return ms.toFixed(1) + '分钟'
  }
  ms = ms / 60
  if (ms < 60) {
    return ms.toFixed(1) + '小时'
  }
  ms = ms / 24
  return ms.toFixed(1) + '天'
}

/**
 * Format bytes as human-readable text.
 * @param bytes Number of bytes.
 * @returns Formatted string.
 */
export function humanBytes(bytes) {
  if (Math.abs(bytes) < 1024) {
    return bytes + ' B'
  }
  const dp = 1
  const units = ['kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  let u = -1
  const r = 10 ** dp
  do {
    bytes /= 1024
    ++u
  } while (Math.round(Math.abs(bytes) * r) / r >= 1024 && u < units.length - 1)
  return bytes.toFixed(dp) + ' ' + units[u]
}

/**
 * Format kilobytes as human-readable text.
 * @param kilobytes Number of kilobytes.
 * @returns Formatted string.
 */
export function humanKiloBytes(kb) {
  if (Math.abs(kb) < 1024) {
    return kb + ' kB'
  }
  const dp = 1
  const units = ['MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  let u = -1
  const r = 10 ** dp
  do {
    kb /= 1024
    ++u
  } while (Math.round(Math.abs(kb) * r) / r >= 1024 && u < units.length - 1)
  return kb.toFixed(dp) + ' ' + units[u]
}
