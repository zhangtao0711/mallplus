import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		hasLogin: false,
		userInfo:''
	},
	mutations: {
		login(state, provider) {
		console.log(provider);
			if (provider && provider!=undefined){
                state.hasLogin = true;
				uni.setStorage({//缓存用户登陆状态
					key: 'userInfo',
					data: provider.userInfo
				})
				state.userInfo = provider;
			}


		},
		logout(state) {
			state.hasLogin = false;
			state.userInfo = '';
			uni.removeStorage({
                key: 'userInfo'
            })
		}
	},
	actions: {

	}
})

export default store
