module.exports = {
  header: '版本日志',
  types: [
    { type: 'feat', section: '新增功能' },
    { type: 'fix', section: 'bug修复' },
    { type: 'merge', hidden: true },
    { type: 'test', hidden: true },
    { type: 'style', hidden: true },
    { type: 'refactor', hidden: true },
    { type: 'supplement', hidden: true },
    { type: 'docs', hidden: true },
    { type: 'build', hidden: true },
    { type: 'ci', hidden: true },
    { type: 'dep', hidden: true },
    { type: 'pref', hidden: true },
    { type: 'revert', hidden: true },
    { type: 'WIP', hidden: true },
    { type: 'chore', hidden: true }
  ],
  // 配置bug地址
  issueUrlFormat: 'http://bug.leimingtech.com/zentao/bug-view-{{id}}.html'
}