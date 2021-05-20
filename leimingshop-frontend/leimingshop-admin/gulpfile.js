var gulp   = require('gulp')
var $      = require('gulp-load-plugins')()
var fs     = require('fs')
var path   = require('path')
var del    = require('del')
var colors = require('colors')
var child_process = require('child_process')

var theme            = {}
var themeList        = require('./src/element-ui/config.js').filter(item => !item.hasBuild)
var styleFileDir     = './src/assets/scss'
var styleFileDirTemp = `${styleFileDir}-temp`
var themeFileDir     = './public/element-theme'
var et               = require('element-theme')
var etOptions        = require('./package.json')['element-theme']
var themeFileName    = etOptions.config.replace(/.*\/(.+\.scss)/, '$1')

/**
 * 构建生成主题
 */
gulp.task('themes', () => {
  if (themeList.length <= 0) { return del(styleFileDirTemp) }

  // 删除临时文件，保证本次操作正常执行
  del(styleFileDirTemp)

  // 拷贝一份scss样式文件夹，作为构建的临时处理文件夹
  child_process.spawnSync('cp', ['-r', styleFileDir, styleFileDirTemp])

  // 拷贝element组件scss变量样式文件至临时处理文件夹中，并修改相应配置信息
  child_process.spawnSync('cp', ['-r', etOptions.config, styleFileDirTemp])
  etOptions.config = `${styleFileDirTemp}/${themeFileName}`

  // 开始构建生成
  fnCreate(themeList)

  function fnCreate (themeList) {
    if (themeList.length >= 1) {
      // 保存当前构建生成的主题对象
      theme = themeList[0]

      console.log('\n')
      console.log(colors.green('-------------------- 待构建，主题 -------------------------'))
      console.log(themeList)
      console.log('\n')
      console.log(colors.green('-------------------- 构建中，主题 -------------------------'))
      console.log(theme)
      console.log('\n')

      // 修改.scss临时文件中的$--color-primary主题变量值
      var data = fs.readFileSync(etOptions.config, 'utf8')
      var result = data.replace(/\$--color-primary:(.*) !default;/, `$--color-primary:${theme.color} !default;`)
      fs.writeFileSync(path.resolve(etOptions.config), result)

      // 修改aui.scss临时文件中引入element组件主题变量文件路径
      var data = fs.readFileSync(`${styleFileDirTemp}/aui.scss`, 'utf8')
      var result = data.replace(new RegExp(`(@import \")(.*\/)(${themeFileName}\";)`), '$1./$3')
      fs.writeFileSync(path.resolve(`${styleFileDirTemp}/aui.scss`), result)

      // 调用element-theme插件，生成element组件主题
      etOptions.out = `${themeFileDir}/${theme.name}`
      et.run(etOptions, () => {
        // 生成后，构建同主题色aui.css项目主题
        gulp.start(['styles'], () => {
          // 递归下一步
          themeList.splice(0, 1)
          fnCreate(themeList)
        })
      })
    } else {
      // 删除临时文件
      del(styleFileDirTemp)
      console.log('\n')
      console.log(colors.green('-------------------- 构建完毕，删除临时文件 -------------------------'))
      console.log(styleFileDirTemp)
      console.log('\n')
      
      // 删除主题不需要的部分文件
      var files = [
        `${themeFileDir}/**/*.css`,
        `!${themeFileDir}/**/index.css`,
        `!${themeFileDir}/**/aui.css`,
        `!${themeFileDir}/**/fonts`
      ]
      del(files)
      console.log(colors.green('-------------------- 构建完毕，删除主题独立组件文件 -------------------------'))
      console.log(files)
      console.log('\n')
    }
  }
})

gulp.task('styles', () => {
  return gulp.src([`${styleFileDirTemp}/aui.scss`])
    .pipe($.sass().on('error', $.sass.logError))
    .pipe($.autoprefixer({
      browsers: etOptions.browsers,
      cascade: false
    }))
    .pipe($.cleanCss())
    .pipe($.rename('aui.css'))
    .pipe(gulp.dest(`${themeFileDir}/${theme.name}`))
})
