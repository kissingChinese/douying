 // 复制文本内容
function copyContent (text) {
    let copyResult = true
    if (!!window.navigator.clipboard) {
      window.navigator.clipboard.writeText(text).then(() => {
      }).catch(() => {
        copyResult = false
        return copyResult;
      })
    } else {
      let inputDom = document.createElement('textarea');
      inputDom.setAttribute('readonly', 'readonly');
      inputDom.value = text;
      document.body.appendChild(inputDom);
      inputDom.select();
      const result = document.execCommand('copy')
      copyResult = result
      document.body.removeChild(inputDom);
    }
    return copyResult;
  }


export default {
    copyContent
}