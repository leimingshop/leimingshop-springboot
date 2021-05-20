// import ViewUI from 'view-design';
// ViewUI.LoadingBar.config({
//     color: '#f6f6f6',
// });

export default ({ app, store }) => {
  app.router.beforeEach((to, from, next) => {
    // 设置条件
//     console.log(app.router,app.router.prototype,'app.router')

    // console.log(to, from, '路由拦截')
    // ViewUI.LoadingBar.start();
    next()
  })

  app.router.afterEach(route => {
      // ViewUI.LoadingBar.finish();
  });

  // console.log(app.router.prototype,'app.router')
  // const originalPush = app.router.prototype.push
  // app.router.prototype.push = function push(location) {
  //   return originalPush.call(this, location).catch(err => err)
  // }


}


