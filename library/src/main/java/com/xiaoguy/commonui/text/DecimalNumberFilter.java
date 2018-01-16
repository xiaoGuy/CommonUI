package com.xiaoguy.commonui.text;

import android.support.annotation.IntRange;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;

/**
 * 限制可以输入的小数位数和可以输入的最大值。<br/>
 * 使用该 InputFilter 后，只能粘贴和剪切一位的字符，前提是粘贴和剪切后的内容要符合要求，否则操作将不生效
 *
 * @author XiaoGai
 * @date 2018/1/9
 */
public class DecimalNumberFilter implements InputFilter {

    private static final String NUMBER = "0123456789";
    private static final String NUMBER_DECIMAL = ".0123456789";
    private static final char DOT = '.';
    private static final String DOT_STRING = ".";
    private static final char O = '0';

    private final DigitsKeyListener mDigitsKeyListener;
    private final int mMaxValue;
    private final int mDecimalCount;

    /**
     * 监听输入的数值是否达到最大值
     */
    public interface OnExceedMaxValueListener {

        /**
         * 当输入的值到达最大值时调用
         */
        void onExceedMaxValue(int maxValue);
    }
    private OnExceedMaxValueListener mOnExceedMaxValueListener;

    public interface OnExceedDecimalCountListener {
        void onExceedPoint(int decimalCount);
    }
    private OnExceedDecimalCountListener mOnExceedDecimalCountListener;

    /**
     * @param maxValue 最大值必须大于等于 1
     * @param decimalCount 不能为复数
     * @throws IllegalArgumentException 如果 maxValue 小于 1 或者 decimalCount 为复数
     */
    public DecimalNumberFilter(@IntRange(from = 1) int maxValue, @IntRange(from = 0) int decimalCount) {
        if (maxValue <= 1) {
            throw new IllegalArgumentException("maxValue cannot less than 1");
        }
        if (decimalCount < 0) {
            throw new IllegalArgumentException("decimalCount cannot less than 0");
        }
        mMaxValue = maxValue;
        mDecimalCount = decimalCount;

        mDigitsKeyListener = DigitsKeyListener.getInstance(
                decimalCount == 0 ? NUMBER : NUMBER_DECIMAL);
    }

    /**
     * @param maxValue 最大值必须大于等于 1
     * @throws IllegalArgumentException 如果 maxValue 小于 1
     */
    public DecimalNumberFilter(int maxValue) {
        this(maxValue, 0);
    }

    public void setOnExceedMaxValueListener(OnExceedMaxValueListener onExceedMaxValueListener) {
        mOnExceedMaxValueListener = onExceedMaxValueListener;
    }

    public void setOnExceedDecimalCountListener(OnExceedDecimalCountListener onExceedDecimalCountListener) {
        mOnExceedDecimalCountListener = onExceedDecimalCountListener;
    }

    /**
     * @param source 本次输入的内容
     * @param start 输入内容的起始位置
     * @param end 输入内容的结束位置
     * @param dest 本次输入前输入框中的内容
     * @param dstart source 往 dest 开始插入的位置
     * @param dend source 往 dest 结束插入的位置
     *
     * @return null 表示原样返回输入的字符，空字符串表示输入的字符不被允许
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        final int length = source.length();
        char inputChar;

        if (length == 0) {
            // 不允许剪切大于 1 位的内容
            if (dend - dstart > 1) {
                return dest.subSequence(dstart, dend);
            }
        }
        // 不允许粘贴大于 1 位的内容
        else if (end - start > 1) {
            return "";
        }

        if (length != 0) {
            // 过滤掉非数字跟点
            CharSequence filteredCs = mDigitsKeyListener.filter(source, start, end, dest, dstart, dend);
            if (filteredCs != null && filteredCs.length() == 0) {
                // 避免在选中状态下输入了不允许的字符时将选中的内容删除
                if (dend - dstart > 0) {
                    return dest.subSequence(dstart, dend);
                }
                return "";
            }
        }

        // 删除或者剪切操作
        if (length == 0) {
            final CharSequence cs = dest.subSequence(dstart, dend);

            StringBuilder sb = new StringBuilder(dest);
            sb.delete(dstart, dend);
            String text = sb.toString();
            int indexOfDot = text.indexOf(DOT_STRING);
            // 如果删除后变为 .xx 或者 0xx 的情况，则不允许删除
            if (text.matches("(\\.\\d*)|(0\\d+)")) {
                return cs;
            }
            if (!TextUtils.isEmpty(text) && indexOfDot == -1 && containsDot(dest)) {
                // 删除 . 后的值如果大于最大值则不能删除
                if (Integer.parseInt(text) > mMaxValue) {
                    if (mOnExceedMaxValueListener != null) {
                        mOnExceedMaxValueListener.onExceedMaxValue(mMaxValue);
                    }
                    return cs;
                } else {
                    return "";
                }
            }
        // 输入操作
        } else {
            CharSequence cs = dest.subSequence(dstart, dend);
            inputChar = source.charAt(0);
            // 以替换的方式输入
            if (dend - dstart > 0) {
                if (inputChar == DOT) {
                    // 第一位字符不能替换成 .
                    if (dstart == 0) {
                        return cs;
                    }
                    if (containsDot(dest)) {
                        return cs;
                    }
                    // 如果替换后小数的位数大于允许的位数，则不允许替换
                    if (dest.length() - dstart - 1 > mDecimalCount) {
                        if (mOnExceedDecimalCountListener != null) {
                            mOnExceedDecimalCountListener.onExceedPoint(mDecimalCount);
                        }
                        return cs;
                    }
                } else {
                    final char replacedChar = dest.charAt(dstart);
                    if (replacedChar == DOT) {
                        // 0.xx 的形式中，. 不能被替换
                        if (dstart == 1 && dest.charAt(0) == O) {
                            return cs;
                        }
                        // 如果替换后的值大于最大值则不允许替换
                        StringBuilder sb = new StringBuilder(dest);
                        sb.replace(dstart, dend, String.valueOf(source.charAt(0)));
                        if (Integer.parseInt(sb.toString()) > mMaxValue) {
                            if (mOnExceedMaxValueListener != null) {
                                mOnExceedMaxValueListener.onExceedMaxValue(mMaxValue);
                            }
                            return cs;
                        }
                    }
                    final char replacement = source.charAt(0);
                    // 当第二位字符不是 . 时，第一位字符不能被替换成 0
                    if (replacement == O && dstart == 0 &&
                            dest.length() >= 2 && dest.charAt(1) != DOT) {
                        return cs;
                    }
                }
            }
            // 以插入的方式添加
            else {
                if (inputChar == DOT) {
                    if (containsDot(dest)) {
                        return "";
                    }
                    // 不允许 . 开头
                    if (dstart == 0) {
                        return "";
                    }
                    // 不允许插入 . 后，小数的位数大于允许的位数
                    if (dest.length() - dstart > mDecimalCount) {
                        if (mOnExceedDecimalCountListener != null) {
                            mOnExceedDecimalCountListener.onExceedPoint(mDecimalCount);
                        }
                        return "";
                    }
                    // 当前输入框中的值已经达到最大值后，不能在末尾输入 .
                    if (dest.length() > 0 && Integer.parseInt(dest.toString()) == mMaxValue
                            && dstart == dest.length()) {
                        return "";
                    }
                } else {
                    // 当前输入框中已经有内容时，不能在开头插入 0
                    if (dstart == 0 && inputChar == O && dest.length() != 0) {
                        return "";
                    }
                    // 第一位是 0 的情况下第二位只能输入 .
                    if (dest.length() == 1 && dest.charAt(0) == O && dstart == 1) {
                        return "";
                    }
                    // 0 和 . 之间不能插入字符
                    if (dest.length() >= 2 && dstart == 1 &&
                            dest.charAt(0) == O && dest.charAt(1) == DOT ) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder(dest);
                    sb.insert(dstart, inputChar);
                    String text = sb.toString();
                    double value;

                    // 禁止输入大于最大值的数
                    int indexOfDot = text.indexOf(DOT_STRING);
                    value = indexOfDot == -1 ? Integer.parseInt(text) : Double.parseDouble(text);
                    if (value > mMaxValue) {
                        if (mOnExceedMaxValueListener != null) {
                            mOnExceedMaxValueListener.onExceedMaxValue(mMaxValue);
                        }
                        return "";
                    }

                    // 禁止输入的小数位数大于允许的位数
                    if (indexOfDot != -1) {
                        int lengthBehindDot = dest.length() - indexOfDot - 1;
                        if (lengthBehindDot >= mDecimalCount) {
                            if (mOnExceedDecimalCountListener != null) {
                                mOnExceedDecimalCountListener.onExceedPoint(mDecimalCount);
                            }
                            return "";
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean containsDot(CharSequence charSequence) {
        return indexOfDot(charSequence) != -1;
    }

    private static int indexOfDot(CharSequence charSequence) {
        return charSequence.toString().indexOf(".");
    }
}
