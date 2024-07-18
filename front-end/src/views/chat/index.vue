<template>
  <div class="chat-room">
    <h2>AI智能助手</h2>
    <div class="messages">
      <div v-for="(message, index) in messages" :key="index" class="message">
        <div :class="{ 'sender': message.sender === '我', 'receiver': message.sender !== '我' }">
          <img :src="message.avatar" width="30" height="30" alt="头像" />
        </div>
        <div :class="{ 'sender': message.sender === '我', 'receiver': message.sender !== '我' }">
          {{ message.content }}
        </div>
      </div>
    </div>
    <div class="input-area">
      <input type="text" placeholder="请输入你要咨询的问题" v-model="question" 
      style="color: white;"
      @keyup.enter.prevent="sendMessage" />
      <button @click.prevent="sendMessage" >发送</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {apiAi} from "../../apis/ai"
export default {
  data() {
    return {
      messages: [
        { sender: '机器人', content:"请问你需要什么帮助", avatar:'https://img2.woyaogexing.com/2019/08/19/6d786834b60b43799bcf7640c64c96ca%21400x400.jpeg' }
      ],
      question:"",
      rep:"",
    };
  },
  methods: {
    sendMessage() {
      if (this.question.trim()) {
        this.messages.push({ sender: '我', content: this.question, avatar: 'https://img2.baidu.com/it/u=1185208597,831327927&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500' });
        apiAi(this.question).then(res=>{

          this.rep = res.data.data
          setTimeout(() => {
              this.messages.push({ sender: '机器人', content: this.rep, avatar:'https://img2.woyaogexing.com/2019/08/19/6d786834b60b43799bcf7640c64c96ca%21400x400.jpeg' });
            }, 1000); // 延迟1秒后自动回复消息
        }).catch(err=>{
          this.question = '';
          this.messages.push({ sender: '机器人', content: "ai出错了", avatar: 'path/to/robot/avatar.png' });
        })
        this.question = '';
      }
    },
  },
};
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 500px;
  width: 900px;
  border: 1px solid #ccc;
  padding-left: 10px;
  margin-left: 150px;
  margin-top : 50px;
}

h2 {
  margin: 0;
  font-size: 20px;
  text-align: center;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #ccc;
  text-align: center;
  line-height: 30px;
  margin-right: 10px;
}

.sender {
  align-self: flex-start;
}

.receiver {
  align-self: flex-end;
}

.content {
  max-width: 600px;
  border-radius: 5px;
  padding: 5px;
  background-color: #f0f0f0;
  display: inline-block;
}

.sender .content {
  background-color: #ffaaff;
  align-self: flex-start;
}

.receiver .content {
  background-color: #aaffff;
  align-self: flex-end;
}

.input-area {
  display: flex;
  padding: 10px;
  color: aliceblue;
}

input {
  flex: 1;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
}

button {
  margin-left: 10px;
  background-color: #1aad19;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  cursor: pointer;
}
</style>
