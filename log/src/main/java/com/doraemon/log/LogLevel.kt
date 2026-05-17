package com.doraemon.log

/**
 * 日志输出级别。
 */
enum class LogLevel {
    /** Verbose: 最详细的日志级别，通常用于记录任何琐碎的细节 */
    V,

    /** Debug: 调试级别日志，用于开发过程中的逻辑跟踪 */
    D,

    /** Info: 普通信息日志，记录应用运行的关键节点 */
    I,

    /** Warn: 警告级别日志，提示潜在的问题或不建议的操作 */
    W,

    /** Error: 错误级别日志，记录已经发生的异常或逻辑错误 */
    E,

    /** WTF: (What a Terrible Failure) 极其严重的错误或不该发生的断言失败 */
    WTF
}
