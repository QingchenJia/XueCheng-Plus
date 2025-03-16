package com.xuecheng.system.constant;

public class SystemConstant {
    // 使用态
    public static final String COMMON_USE = "1";
    // 删除态
    public static final String COMMON_DELETE = "0";
    // 暂时态
    public static final String COMMON_TEMP = "-1";
    // 对象审核未通过
    public static final String OBJECT_AUDIT_FAIL = "002001";
    // 对象未审核
    public static final String OBJECT_AUDIT_NO = "002001";
    // 对象审核通过
    public static final String OBJECT_AUDIT_SUCCESS = "002001";
    // 图片类型资源
    public static final String RESOURCE_IMAGE = "001001";
    // 视频类型资源
    public static final String RESOURCE_VIDEO = "001002";
    // 其他类型资源
    public static final String RESOURCE_OTHER = "001003";
    // 课程审核未通过
    public static final String COURSE_AUDIT_FAIL = "202001";
    // 课程审核未提交
    public static final String COURSE_AUDIT_NO = "202002";
    // 课程审核已提交
    public static final String COURSE_AUDIT_SUBMIT = "202003";
    // 课程审核通过
    public static final String COURSE_AUDIT_SUCCESS = "202004";
    // 免费课程
    public static final String COURSE_FREE = "201000";
    // 收费课程
    public static final String COURSE_CHARGE = "201002";
    // 初级课程
    public static final String COURSE_LEVEL_LOW = "204001";
    // 中级课程
    public static final String COURSE_LEVEL_MIDDLE = "204002";
    // 高级课程
    public static final String COURSE_LEVEL_HIGH = "204003";
    // 录播类型课程
    public static final String COURSE_MODE_RECORD = "200002";
    // 直播类型课程
    public static final String COURSE_MODE_LIVE = "200003";
    // 课程未发布
    public static final String COURSE_PUBLISH_NO = "203001";
    // 课程已发布
    public static final String COURSE_PUBLISH_ON = "203002";
    // 课程下线
    public static final String COURSE_PUBLISH_OFF = "203003";
    // 订单未支付
    public static final String ORDER_UNPAID = "600001";
    // 订单已支付
    public static final String ORDER_PAID = "600002";
    // 订单已关闭
    public static final String ORDER_CANCEL = "600003";
    // 订单已退款
    public static final String ORDER_RETURN = "600004";
    // 订单已完成
    public static final String ORDER_FINISH = "600005";
    // 作业未提交
    public static final String HOMEWORK_NO = "306001";
    // 作业待批改
    public static final String HOMEWORK_WAIT = "306002";
    // 作业已批改
    public static final String HOMEWORK_CORRECT = "306003";
    // 消息未通知
    public static final String MESSAGE_NOTIFICATION_NO = "003001";
    // 消息通知成功
    public static final String MESSAGE_NOTIFICATION_SUCCESS = "003002";
    // 支付状态未支付
    public static final String PAY_INFO_UNPAID = "601001";
    // 支付状态已支付
    public static final String PAY_INFO_PAID = "601002";
    // 支付状态已退款
    public static final String PAY_INFO_RETURN = "601003";
    // 购买课程业2务
    public static final String SERVICE_COURSE_BUY = "60201";
    // 获取资料业务
    public static final String SERVICE_INFORMATION_GET = "60202";
    // 微信支付方式
    public static final String THIRD_WECHAT_PAY = "603001";
    // 支付宝支付方式
    public static final String THIRD_ALIPAY_PAY = "603002";
    // 选课类型免费课程
    public static final String CHOOSE_COURSE_FREE = "700001";
    // 选课类型收费课程
    public static final String CHOOSE_COURSE_CHARGE = "700002";
    // 选课成功
    public static final String CHOOSE_STATUS_SUCCESS = "701001";
    // 选课后待支付
    public static final String CHOOSE_STATUS_UNPAID = "701002";
    // 正常学习
    public static final String CHOOSE_QUALIFICATION_RIGHT = "702001";
    // 没有选课或选课后待支付
    public static final String CHOOSE_QUALIFICATION_ERROR = "702002";
    // 课程到期未续费或重新支付
    public static final String CHOOSE_QUALIFICATION_OVERDUE = "702003";
}
