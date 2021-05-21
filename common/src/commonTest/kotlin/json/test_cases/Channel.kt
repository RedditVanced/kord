package json.test_cases

//language=JSON
const val DM_CHANNEL = """
{
  "last_message_id": "3343820033257021450",
  "type": 1,
  "id": "319674150115610528",
  "recipients": [
    {
      "username": "test",
      "discriminator": "9999",
      "id": "82198898841029460",
      "avatar": "33ecab261d4681afa4d85a04691c4a01"
    }
  ]
}
"""

//language=JSON
const val CHANNEL_CATEGORY = """
{
  "permission_overwrites": [],
  "name": "Test",
  "parent_id": null,
  "nsfw": false,
  "position": 0,
  "guild_id": "290926798629997250",
  "type": 4,
  "id": "399942396007890945"
}
"""

//language=JSON
const val GROUP_DM_CHANNEL = """
    {
  "name": "Some test channel",
  "icon": null,
  "recipients": [
    {
      "username": "test",
      "discriminator": "9999",
      "id": "82198898841029460",
      "avatar": "33ecab261d4681afa4d85a04691c4a01"
    },
    {
      "username": "test2",
      "discriminator": "9999",
      "id": "82198810841029460",
      "avatar": "33ecab261d4681afa4d85a10691c4a01"
    }
  ],
  "last_message_id": "3343820033257021450",
  "type": 3,
  "id": "319674150115710528",
  "owner_id": "82198810841029460"
}
"""

//language=JSON
const val GUILD_NEWS_CHANNEL = """
    {
  "id": "41771983423143937",
  "guild_id": "41771983423143937",
  "name": "important-news",
  "type": 5,
  "position": 6,
  "permission_overwrites": [],
  "nsfw": true,
  "topic": "Rumors about Half Life 3",
  "last_message_id": "155117677105512449",
  "parent_id": "399942396007890945"
}
"""

//language=JSON
const val GUILD_TEXT_CHANNEL = """
{
  "id": "41771983423143937",
  "guild_id": "41771983423143937",
  "name": "general",
  "type": 0,
  "position": 6,
  "permission_overwrites": [],
  "rate_limit_per_user": 2,
  "nsfw": true,
  "topic": "24/7 chat about how to gank Mike #2",
  "last_message_id": "155117677105512449",
  "parent_id": "399942396007890945"
}
"""

//language=JSON
const val GUILD_VOICE_CHANNEL = """
{
  "id": "155101607195836416",
  "guild_id": "41771983423143937",
  "name": "ROCKET CHEESE",
  "type": 2,
  "nsfw": false,
  "position": 5,
  "permission_overwrites": [],
  "bitrate": 64000,
  "user_limit": 0,
  "parent_id": null
}
"""

//language=JSON
const val STORE_CHANNEL = """
{
  "id": "41771983423143937",
  "guild_id": "41771983423143937",
  "name": "buy dota-2",
  "type": 6,
  "position": 0,
  "permission_overwrites": [],
  "nsfw": false,
  "parent_id": null
}
"""