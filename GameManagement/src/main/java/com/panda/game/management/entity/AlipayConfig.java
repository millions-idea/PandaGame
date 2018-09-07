package com.panda.game.management.entity;

public interface AlipayConfig {
	// 商户appid
	public static String APPID = "2016080800192393";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCrrV5ChD6H4VO8oFaz9qgmusPbL/dEwnwl+/r+PP4OPHtoort5n4WFCL/3w8oTDmY29LNCbms4r/1i4j501npQnXh0YcydkRQluMPX2h6nlpX4laOh/0+3S9pMA2lr1tLnv/tIIsh7eZ6EIA6/F+OFgFy1yhG7ccKp9GYpEiquRuDkkhb77Uy2jdKF/oU/d+8Q7tHiLAvusyj2to7wyhWRJhWFPykjcgbH3mFX44oFiEN70uir3lZvcsdngbhtLh/IevVgfa9ueeITDzfO1FQHj0fO2jIWWaxxw+WV/T6uik8/MZ8v26QN2NM3bSkoa5kXxpAN2WkAxYfLYLbfTiAXAgMBAAECggEAfuFe7OI4JMzPynTmH5OIqzcVUYWdxl/GyXv2ALirO3JP8wFydW0EjVHuLvuw+WyG9s7bPZnF/Nt09gQoDMoHFFWNlNZDF6EFtutbMsiuJ0YQCOzvRucez2VsLQnIfD8FGOq2mJW8fiVgll3rCChrq1s3KdPyTwrAyEMszeXzTWE3JczT/xiX6lLMjXNN2O5yxt5aZYhuTX2LIq/Az4q+JXF2m71Nm5D27rbDIWEiL427Dks+QboWPe+Wrijkfgizz28t0GymPQU5lvBwhYl3LopkxYkadynYdmD5teqDBp1scVhpDRQSkehAslTK/xGpCf3KhjLoXvHO9+oukkkS4QKBgQDUOIRjPTlstwmgS+ol0hUHSc8IxwFTfqIrenCgQEgddXjq9qQemIKkQd3z7XQCkdLnwzNLvr6/EFWlGYiEC7pVWRnrAjbYrfoFCpvDKZx8sAc2hw4i7eETcmkbWNxj4rph4tKSWdV/qQcAA2ORIGCKaLv20HE1FQ+y6Ls5Xvt+bQKBgQDPF7nuSRySak7SAvGew+/wxu2MEmDX0osZWTSkNmBzh7B8MsoYN2879qIw3cf8kjc8lHWv17SYaEeKShXxWOeoDwBP/Rko2x6D268j8EfHJcyBag6ngDobHvl0eXF7jHMJjHaWwQZVlERVa4aILRm1LF+KpwV7AldR4n4Mb2P2EwKBgQCwxbsQ5ui0c+riyRlrE4lMnuCDkTC7mdn/mWfDNIhdcYKdRYEDs0nvBKabDwn5MfqZ7vEFgQdmI3cTpScfxp0ZhrSeGyQQbSq7lFCWykx9WCtJ8kmGMtuu1ThT/TrnQO67prJ8EbRTODqKekgvlOaVpBQdzRbffoGrJ0z8xu96KQKBgBd41PYRC0/9BJoBA2m/Q37CzUHHA3ryvhlNZw4Fq8DsbDBngUhlYBInzhJD0Nrrp58SJJSdmjZ7+z/NTddhAECoEpz+Ts+2pirDx4IHRdtoiVlRr/+EJRHV3Og6q4YZUSCP7xD/J7yjZWtZGJJuqi9CrEGG1LQwe70oMqkuJuJJAoGAQSTX/HLCV/W2uqUWawz36ak97+6rx1uH4IPOZNJD/bjwGC6AuUC60aqOlW5PGCQcus9Lh6NMPul89u5ISfCqdPrUkOK1oVMU2saj+J7rDcx6bGlAcvZidrb0fq7ktfZr7XRJYwWHSdsh5MnD4hbk5InuCL2nXph5zQzlBP90srw=";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://bkz4ym.natappfree.cc/payment/appNotify";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://bnqmpa.natappfree.cc/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";
	// 请求网关地址
	public static String URL = "https://openapi.alipaydev.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvD1EsnewGM+zmV44L9vrJ/YVV3vxCdJqD1zqyNilOCcO/rjcbYrRBBwHuoRj2EicWrdxRksi9Pm9XcE5l9yDImkvnY+wlcma2HRDAaU/Vzid6Qkb8afzoNWCkfz1RNXX0PJXu1UJhKQV4GE0H2iNcf9qWHPTJhNO/IQo1EhI/ovqhlgTe9Jr8dCfn7gUwLyrv8OfKWRpfpQVmRDsWhxzEAW3x2OY/+lTyJUsUWNNiap4CI+3mrukWRfoeabkkMv3bPG63shwI0klyQK8koU8+xTPaKZl6moOYkASgiRRvNThe719ka74LMMgeabWlUBp94m6IYol9jnxCVvKe/wwIQIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";
}
