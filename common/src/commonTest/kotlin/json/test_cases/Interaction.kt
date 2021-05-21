package json.test_cases

//language=JSON
const val GROUP_SUB_COMMAND = """
{
  "version": 1,
  "type": 2,
  "token": "hunter2",
  "member": {
    "user": {
      "username": "Hope",
      "public_flags": 64,
      "id": "695549908383432716",
      "discriminator": "9000",
      "avatar": "68922a048551bb4b0dda88b65e85d8f1"
    },
    "roles": [
      "556525712308305940"
    ],
    "premium_since": null,
    "permissions": "2147483647",
    "pending": false,
    "nick": null,
    "mute": false,
    "joined_at": "2020-04-03T09:35:54.879000+00:00",
    "is_pending": false,
    "deaf": false
  },
  "id": "793442788670832640",
  "guild_id": "556525343595298817",
  "data": {
    "options": [
      {
        "options": [
          {
            "options": [
              {
                "value": 1,
                "name": "testint"
              }
            ],
            "name": "groupsubcommand"
          }
        ],
        "name": "group"
      }
    ],
    "name": "testsubcommands",
    "id": "792107855418490901"
  },
  "channel_id": "587324906702766226",
  "application_id": "297153970613387264"
}
"""

//language=JSON
const val ROOT_COMMAND = """
{
  "version": 1,
  "type": 2,
  "token": "hunter2",
  "member": {
    "user": {
      "username": "Hope",
      "public_flags": 64,
      "id": "695549908383432716",
      "discriminator": "9000",
      "avatar": "68922a048551bb4b0dda88b65e85d8f1"
    },
    "roles": [
      "556525712308305940"
    ],
    "premium_since": null,
    "permissions": "2147483647",
    "pending": false,
    "nick": null,
    "mute": false,
    "joined_at": "2020-04-03T09:35:54.879000+00:00",
    "is_pending": false,
    "deaf": false
  },
  "id": "793442788670832640",
  "guild_id": "556525343595298817",
  "data": {
    "options": [
      {
        "value": 1,
        "name": "testint"
      }
    ],
    "name": "testsubcommands",
    "id": "792107855418490901"
  },
  "channel_id": "587324906702766226",
  "application_id": "297153970613387264"
}
"""

//language=JSON
const val SLASH_COMMAND_PERMISSIONS_UPDATE = """
{
  "id": "833008574669651989",
  "application_id": "535129406650318860",
  "guild_id": "809471441719787602",
  "permissions": [
    {
      "id": "827126703301066792",
      "type": 1,
      "permission": true
    }
  ]
}

"""

//language=JSON
const val SUB_COMMAND = """
{
  "version": 1,
  "type": 2,
  "token": "hunter2",
  "member": {
    "user": {
      "username": "Hope",
      "public_flags": 64,
      "id": "695549908383432716",
      "discriminator": "9000",
      "avatar": "68922a048551bb4b0dda88b65e85d8f1"
    },
    "roles": [
      "556525712308305940"
    ],
    "premium_since": null,
    "permissions": "2147483647",
    "pending": false,
    "nick": null,
    "mute": false,
    "joined_at": "2020-04-03T09:35:54.879000+00:00",
    "is_pending": false,
    "deaf": false
  },
  "id": "793442788670832640",
  "guild_id": "556525343595298817",
  "data": {
    "options": [
      {
        "options": [
          {
                "value": 1,
                "name": "testint"
              }
            ],
        "name": "subcommand"
      }
    ],
    "name": "testsubcommands",
    "id": "792107855418490901"
  },
  "channel_id": "587324906702766226",
  "application_id": "297153970613387264"
}
"""