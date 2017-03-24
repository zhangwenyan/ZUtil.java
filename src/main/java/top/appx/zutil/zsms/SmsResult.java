package top.appx.zutil.zsms;

/**
 * Created by lhzxd on 2017/3/5.
 */
class SmsResult {
    public R alibaba_aliqin_fc_sms_num_send_response;
    public Error_response error_response;
}
class Error_response
{
    public String code;
    public String msg;
    public String sub_msg;

}
class R
{
    public Result result;
}
class Result
{
    public boolean success;
}
