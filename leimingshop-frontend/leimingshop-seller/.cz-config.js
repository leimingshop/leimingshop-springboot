module.exports = {
  // 提交模板
  types: [
    { value: 'feat', name: 'feat: 新增功能' },
    { value: 'fix', name: 'fix: bug修复' },
    { value: 'merge', name: 'merge: 分支合并 Merge branch ? of ?' },
    { value: 'test', name: 'test: 新增测试用例或是更新现有测试的提交' },
    { value: 'style', name: 'style: 不影响程序逻辑的代码修改(修改空白字符，格式缩进，补全缺失的分号等，没有改变代码逻辑)的提交' },
    { value: 'refactor', name: 'refactor: 重构代码(既没有新增功能，也没有修复 bug)' },
    { value: 'supplement', name: 'supplement: 增强程序逻辑的补充提交' },
    { value: 'docs', name: 'docs: 文档更新相关的提交' },
    { value: 'build', name: 'build: 主要目的是修改项目构建系统(例如 glup，webpack，rollup 的配置等)的提交' },
    { value: 'ci', name: 'ci: 主要目的是修改项目继续集成流程(例如 Travis，Jenkins，GitLab CI，Circle 等)的提交' },
    { value: 'dep', name: 'dep: 依赖版本更新或者修改相关提交' },
    { value: 'perf', name: 'perf: 性能优化的提交' },
    { value: 'revert', name: 'revert: 回滚某个更早之前的提交' },
    { value: 'WIP', name: 'WIP: 还在开发进程中(Work In Progress)' },
    { value: 'chore', name: 'chore: 不属于以上类型的其他类型' }
  ],
  // scope选项
  // scopes: [{ name: 'accounts' }],
  // 汉化交互式信息
  messages: {
    type: '为你的此次提交选择一种提交类型:',
    scope: '\n输入此次修改的文件或者功能模块(optional):',
    subject: '为此次提交输入一个简短的标题(20个字以内):',
    body: '请提供一个对此次提交的详细描述，输入 "|" 即可换行:',
    breaking: '列举此次提交带来的重大变更(breaking change) (optional):',
    footer: '列举此次提交修复的bug编号，如#1234 (optional)',
    confirmCommit: '是否确认此次变更提交以上内容所构成的commit记录？'
  },
  // 短标题字数限制
  subjectLimit: 30
}