package com.gitlab.kordlib.core.behavior

import equality.EntityEqualityTest
import mockKord

internal class WebhookBehaviorTest : EntityEqualityTest<WebhookBehavior> by EntityEqualityTest({
    val kord = mockKord()
    WebhookBehavior(it, kord)
})