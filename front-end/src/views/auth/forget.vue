<template>
    <div>
        <v-card class="mx-auto" color="#36393f" max-width="650" theme="dark" variant="flat">
            <v-sheet color="#202225">
                <v-card-item>
                    <template v-slot:prepend>
                        <v-card-title>
                            <v-icon icon="mdi-account" start></v-icon>
                            忘记账号
                        </v-card-title>
                    </template>
                </v-card-item>
            </v-sheet>

            <v-card class="ma-4" color="#2f3136" rounded="lg" variant="flat">
                <v-card-item>
                    <v-card-title class="text-body-2 d-flex align-center">
                        <v-icon color="#949cf7" icon="mdi-step-forward" start></v-icon>

                        <span class="text-medium-emphasis font-weight-bold">{{ stepData[step].title }}</span>

                        <v-spacer></v-spacer>
                        <v-chip class="ms-2 text-medium-emphasis" color="grey-darken-4" size="small"
                            variant="flat">{{ step }}</v-chip>
                    </v-card-title>
                </v-card-item>

                <v-divider></v-divider>

                <v-window v-model="step">
                    <v-window-item :value="1">
                        <v-form :disabled="isLoading">
                            <v-card-text>
                                <v-text-field v-model="registerInfo.email" label="邮箱" hint="tips: 邮箱必须是真实有效的，它将用于登录账号使用。"
                                    placeholder="请输入邮箱"></v-text-field>
                                <v-row no-gutters>
                                    <v-col>
                                        <v-img :src="captchaImg" @click="getCaptchaImg"></v-img>
                                    </v-col>
                                    <v-col>
                                        <v-text-field v-model="registerInfo.captchaCode" label="图形验证码" hide-details
                                            placeholder="请输入图形验证码"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-btn color="blue" block :variant="'flat'" @click="getEmailCode()">获取邮箱验证码</v-btn>
                            </v-card-text>
                            <v-otp-input v-model="registerInfo.code" placeholder="0"></v-otp-input>
                        </v-form>

                    </v-window-item>

                    <v-window-item :value="2">
                        <v-card-text>
                            <v-text-field v-model="registerInfo.newPassword" label="请输入新密码" type="password"></v-text-field>
                            <v-text-field v-model="registerInfo.confirmPassword" label="请确认新密码"
                                type="password"></v-text-field>
                            <span class="text-caption text-grey-darken-1">
                                tips: 请为您的账号设置一个新密码
                            </span>
                        </v-card-text>
                    </v-window-item>

                    <v-window-item :value="3">
                        <div class="pa-4 text-center" style="color: greenyellow;">
                            <v-icon :size="60">mdi-check-circle</v-icon>
                            <h3 class="text-h6 font-weight-light mb-2">
                                找回成功
                            </h3>
                            <span class="text-caption text-grey">注意保存好密码!</span>
                        </div>

                    </v-window-item>
                </v-window>
                <v-card-actions>
                    <v-btn v-if="step == 2" variant="text" @click="step--" :loading="isLoading">
                        Back
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn v-if="step < 3" color="blue" variant="flat" @click="stepData[step].next?stepData[step].next():(step++)" :loading="isLoading">
                        Next
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-card>
    </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { VOtpInput } from 'vuetify/labs/VOtpInput';
import { apiCheckCode, apiForgetPassword, apiGetCode } from '../../apis/user/auth';
import buildUtils from '../../utils/buildUtil';
const step = ref(1)
const stepData = ref({
    1: {
        title: "邮箱验证",
        next: ()=>{
            vertifyCode()
        }
    },
    2: {
        title: "设置新密码",
        next: ()=>{
            setPassword()
        }
    },
    3: {
        title: "找回成功"
    }
})
const {showMessage, closeEvent} = defineProps({
  showMessage: {
    type: Function,
    default:()=>{}
  },
  closeEvent: {
    type: Function,
    default: ()=>{}
  }
})
const isLoading = ref(false)
const captchaImg = ref()
const getCaptchaImg = () => {
    registerInfo.uuid = buildUtils.guid()
    captchaImg.value = apiGetCode(1, registerInfo.uuid)
}
const registerInfo = reactive({
    email: "",
    nickName: "",
    code: "",
    captchaCode: "",
    password: "",
    confirmPassword: "",
    uuid: ""
})

getCaptchaImg()
// =========== methods 函数 ==============
const getEmailCode = () => {
    apiGetCode(0, { ...registerInfo, code: registerInfo.captchaCode }).then(({ data }) => {
        showMessage(data.message, data.state?"successs":"error")
        if (!data.state) {
            getCaptchaImg()
            return;
        }
    })
}

const vertifyCode = () => {
    isLoading.value = true
    apiCheckCode({...registerInfo}).then(({ data }) => {
        isLoading.value = false
        
        if(!data.state) {
            showMessage("邮箱验证码无效", 'red')
            getCaptchaImg()
            return;
        }
        step.value++
    })
}

const setPassword = ()=>{
    apiForgetPassword(registerInfo).then(({data})=>{
        if(!data.state) {
            showMessage(data.message, "error")
            return;
        }
        step.value++
    })
}
</script>