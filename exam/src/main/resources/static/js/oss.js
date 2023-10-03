let ossClient = new OSS({
    // yourRegion填写Bucket所在地域。以华东1（杭州）为例，Region填写为oss-cn-hangzhou。
    region: 'oss-cn-chengdu',
    // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
    accessKeyId: 'LTAI5tF32STKKpozP6FVcALw',
    accessKeySecret: '6emEiRuJZBsL9Hk1fkg1oxUAxTQgLS',
    // 填写Bucket名称。
    bucket: 'investigate-file'
});