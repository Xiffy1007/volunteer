let ajaxTimes=0;
const POST="POST";
export const request =(params) =>{
	ajaxTimes++;
	wx.showLoading({
	title: '加载中',
	mask: true
	})
	const baseUrl = "http://localhost:8088"; 
	return new Promise((resolve,reject) =>{
		wx.request( {
			url: baseUrl + params.url,
			method:params.method,
		  data:params.method==POST?JSON.stringify(params.data):params.data,
			success:(result) => {
				resolve(result.data);
			},
			fail: (err) =>{
				 reject(err) ;
			},
			complete: () =>{
				 ajaxTimes--;
				 if(ajaxTimes===0){
					 wx.hideLoading();
				 }
				}
			})
		});
}


/**
 * wx login封装
 */
export const getWxLogin=()=>{
  return new Promise((resolve,reject)=>{
    wx.login({
      timeout: 5000,
      success:(res)=>{
        resolve(res)
      },
      fail:(err)=>{
        reject(err)
      }
    })
  });
}
