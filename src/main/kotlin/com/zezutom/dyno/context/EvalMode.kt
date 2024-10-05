package com.zezutom.dyno.context

enum class EvalMode {
    // Strict mode will throw an exception as soon as it encounters an error when building the context.
    Strict,

    // Lenient mode will allow the context to be built even if there are errors.
    Lenient,
}
