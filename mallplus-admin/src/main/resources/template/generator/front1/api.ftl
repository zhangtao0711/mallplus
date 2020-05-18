
{
	path: '/@/views/${moduleName}/${changeClassName}/index',
	name: '${changeClassName}',
	meta: {
		title: '${prefix}',
		showflg: true,
		roles: "2",
		level1: "${moduleName}_level",
		level2: "${changeClassName}_level"
	},
	component: () =>
		import('@/views/${moduleName}/${changeClassName}/index'),
},

{
	path: '/@/views/${moduleName}/${changeClassName}/components/add',
	name: 'add${className}',
	meta: {
		title: '新增${prefix}',
		showflg: false,
		roles: "2",
		level1: "${moduleName}_level",
		level2: "${changeClassName}_level"
	},
	component: () =>
		import('@/views/${moduleName}/${changeClassName}/components/add'),
},

{
	path: '/@/views/${moduleName}/${changeClassName}/components/edit',
	name: 'edit${className}',
	meta: {
		title: '编辑${prefix}',
		showflg: false,
		roles: "2",
		level1: "${moduleName}_level",
		level2: "${changeClassName}_level"
	},
	component: () =>
		import('@/views/${moduleName}/${changeClassName}/components/edit'),
},

{
	path: '',
	name: '2',
	meta: {
		title: '删除${prefix}',
		showflg: false,
		roles: "2",
		level1: "${moduleName}_level",
		level2: "${changeClassName}_level"
	},
},


