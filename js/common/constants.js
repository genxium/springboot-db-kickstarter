'use-strict';

const IS_TESTING = (undefined == process.env.NODE_ENV || null == process.env.NODE_ENV || '' == process.env.NODE_ENV || 'development' == process.env.NODE_ENV);
exports.IS_TESTING = IS_TESTING;

const IS_STAGING = ('staging' == process.env.NODE_ENV);
exports.IS_STAGING = IS_STAGING;

const NOT_IN_PRODUCTION = ('production' != process.env.NODE_ENV);
exports.NOT_IN_PRODUCTION = NOT_IN_PRODUCTION;

exports.USE_FSERVER = ('true' == process.env.FSERVER);

exports.HTTP_PROTO = (NOT_IN_PRODUCTION ? 'http' : 'http');
exports.HTTP_PORT = (NOT_IN_PRODUCTION ? (IS_STAGING ? ":8008" : ":9099") : "");

exports.AUTH_CHANNEL = {
  EMAIL: 0,
  SMS: 1,
  WECHAT_MOBAPP: 2,
  WECHAT_WEBAPP: 3,
  WECHAT_PUBSRV: 4,
  WECHAT_PUBSUB: 5,
}

exports.AUTHENTICATION_STATE = {
  NONE: 0,
  EMAIL: (1 << 0),
  SMS: (1 << 1),
  WECHAT: (1 << 2),
}

exports.PURCHASE = {
  STATE: {
    CREATED: 0,
    PAID: 1,
  },
  CREATION_LIMIT: {
    N: 5,
    INTERVAL_MILLIS: 30000, // 30 seconds
  },
}

const TICKET = {
  CONSUMPTION: {
    TYPE: {
      LAUNCH: 1,
      UPTOKEN_REQ: 2,
      REPORT: 3,
    },
    LIMIT: {},
  }
};

for (let k in TICKET.CONSUMPTION.TYPE) {
  const ticketConsumptionType = TICKET.CONSUMPTION.TYPE[k];
  TICKET.CONSUMPTION.LIMIT[ticketConsumptionType] = 1;
}

exports.TICKET = TICKET;

exports.PAYMENT_CHANNEL = {
  CHANNEL_WXPAY_MOBAPP: 0,
  CHANNEL_WXPAY_PUBSRV: 1,
}

exports.WS_ERR = "wsErr";

exports.SOCKET_DOT_IO = {
  CONNECT: "connect",
  DISCONNECT: "disconnect",
};

exports.CMD_ENTITY_TYPE = {
  PURCHASE: 0,
  TICKET: 1,
  UPTOKEN: 2,
  WECHAT_PUBSRV_PAYMENT_RESPONSE: 3,
};

exports.CMD_ACT = {
  CREATE: 0,
  DELETE: 1,
  CHANGE: 2,
  QUERY: 3,
  JOIN: 4,
  LEAVE: 5,
  UPSERT: 6,
  LAUNCH: 7,
  RESPOND: 8,
  REPORT: 9,
  REQUEST: 10,
};

const REGEX = {
  EMAIL: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  PHONE: /^\+?[0-9]{8,14}$/,
  ADDRESS_META: /^.{5,100}$/,
  LNG_LAT_TEXT: /^[0-9]+(\.[0-9]{4,6})$/,
  SEO_KEYWORD: /^.{2,50}$/,

  PASSWORD: /^[a-zA-Z0-9_#&*$]{6,50}$/,

  ORG_PRINCIPAL: /^[a-zA-Z0-9]{4,50}$/,
  ORG_DISPLAY_NAME: /^.{6,100}$/,

  SUBORG_DISPLAY_NAME: /^.{4,100}$/,

  ADMIN_PRINCIPAL: /^[a-zA-Z0-9_]{4,50}$/,

  WRITER_PRINCIPAL: /^[a-zA-Z0-9_]{6,50}$/,
  WRITER_DISPLAY_NAME: /^.{3,100}$/,

  PLAYER_PRINCIPAL: /^[a-zA-Z0-9_]{6,50}$/,
  PLAYER_DISPLAY_NAME: /^.{3,100}$/,

  PRICE_CURRENCY: /^[0-9]{1}$/,
  PRICE_VALUE_CENTS: /^[0-9]+$/,

  KEYWORD: /^[^\n\t]{2,20}$/,
};

exports.REGEX = REGEX;

exports.KEYBOARD_CODE = {
  RETURN: 13
}

exports.ARTICLE = {
  CATEGORY: {
    NONE: 0,
    LATEST: 1,
    MISC: 2,
  },
  STATE: {
    NONE: 0,
    CREATED: 1,
    PENDING: 2,
    APPROVED: 3,
    DENIED: 4,
    ADMIN_SUSPENDED: 5,
    AUTHOR_SUSPENDED: 6,
  },
  CREATION_LIMIT: {
    N_NOT_APPROVED: 5,
    N_KEYWORDS: 10,
  },
};

exports.RET_CODE = {
  OK: 1000,
  FAILURE: 1001,
  REDIRECTED: 1002,

  DUPLICATED: 2002,
  TOKEN_EXPIRED: 2003,
  INCORRECT_CAPTCHA: 2004,
  NONEXISTENT_PRINCIPAL: 2005,
  INCORRECT_PASSWORD: 2006,
  INVALID_EMAIL_LITERAL: 2007,
  NO_ASSOCIATED_EMAIL: 2008,
  SEND_EMAIL_TIMEOUT: 2009,
  NONEXISTENT_ORG: 2011,
  INSUFFICIENT_MEM_TO_ALLOCATE_CONNECTION: 2012,
  INVALID_TICKET_CONSUMPTION_TYPE: 2014,
  TICKET_CONSUMPTION_LIMIT_EXCEEDED: 2015,
  NONEXISTENT_WRITER: 2018,
  NONEXISTENT_ARTICLE: 2019,
  NEW_ARTICLE_LIMIT_EXCEEDED: 2020,
  UPTOKEN_INVALID_MIME_TYPE_GROUP: 2021,

  NONEXISTENT_SUBORG: 2022,
  NONEXISTENT_WRITER_SUBORG_BINDING: 2023,

  PASSWORD_RESET_CODE_GENERATION_PER_EMAIL_TOO_FREQUENTLY: 4000,
  CAPTCHA_GENERATION_PER_PHONE_TOO_FREQUENTLY: 4001,
  PURCHASE_CREATION_TOO_FREQUENTLY: 4002,

  NOT_IMPLEMENTED_YET: 65535,
};

exports.OSS_DELETION_RESULT_CODE = {
  UNKNOWN: 10000,
  DELETED: 10001,
  NOT_DELETED_NO_LONGER_EXISTING: 10002,
  NOT_DELETED_PERMISSION_DENIED: 10003,
};

const ROUTE_PATHS = {
  ROOT: "/",
  API_V1: "/Api/V1",

  BASE: "",
  ADMIN: "/Admin",
  WRITER: "/Writer",
  PLAYER: "/Player",
  ORG: "/Org",
  SUBORG: "/Suborg",
  PATH: "/Path",

  ARTICLE: "/Article",

  IMAGE: "/Image",
  VIDEO: "/Video",

  UPTOKEN: "/Uptoken",
  FETCH: "/Fetch",

  LIVECHAT: "/Livechat",
  CONSOLE: "/Console",
  SKU: "/Sku",

  CREDENTIALS: "/Credentials",
  PAGINATION: "/Pagination",

  HOME: "/Home",
  ADD: "/Add",
  SAVE: "/Save",
  SUBMIT: "/Submit",
  SUSPEND: "/Suspend",
  EDIT: "/Edit",
  DELETE: "/Delete",

  DETAIL: "/Detail",
  LIST: "/List",
  LOGIN: "/Login",
  LOGOUT: "/Logout",

  SYNC_CB: "/SyncCb",

  INT_AUTH_TOKEN: "/IntAuthToken",

  WECHAT_PUBSRV: "/WechatPubsrv",
  PAYMENT: "/Payment",
  NOTIFY: "/Notify",

  // The following fields are deliberately kept "upperCase" instead of using "UpperCase".
  CORS: "/cors",
  PUBLIC: "/public",
  ICON: "/icon",
  CLIPART: "/clipart",
};

exports.ROUTE_PATHS = ROUTE_PATHS;

const ROUTE_PARAMS = {
  API_VER: "/Api/:version",
  ACT: "/:act",
};

exports.ROUTE_PARAMS = ROUTE_PARAMS;

exports.ICON = {
  CLIPART_TRIANGLE: '/clipart-triangle.svg',
};

exports.SYNC_CB_ACT = {
  WECHAT_PUBSRV_LOGIN: "wechatPubsrvLogin",
};

exports.ICP_RECORD_NUMBER = "";

const COMPANY_NAME = "Demo";
const APP_NAME = "Demo";

const CURRENCY = {
  RMB: 1,
};

exports.CURRENCY = CURRENCY;

const ZH_CN = {
  ALL: "??????",
  NO_MORE: "????????? :(",
  PLAYER_CONSOLE: "???????????????",
  WRITER_CONSOLE: "???????????????",
  ADMIN_CONSOLE: "??????????????????",
  CENTRAL_AUTH_CONSOLE: "????????????",

  PLAYER_LOGIN: "????????????",
  ADMIN_LOGIN: "???????????????",
  WRITER_LOGIN: "????????????",
  PLEASE_INPUT_TITLE: "???????????????",

  PLEASE_INPUT_PRINCIPAL: "??????????????????",
  PLEASE_INPUT_PASSWORD: "???????????????",
  NONEXISTENT_PRINCIPAL: "??????????????????",
  INCORRECT_PASSWORD: "???????????????",

  WRITER_PRINCIPAL: "????????????(??????????????????????????????)",
  WRITER_LIST: "????????????",
  WRITER_DISPLAY_NAME: "????????????",
  WRITER_PASSWORD_INPUT_HINT: "??????????????????????????????????????????",
  ADD_WRITER: "????????????",
  EDIT_WRITER: "????????????",

  ARTICLE_TITLE: "????????????",
  ARTICLE_LIST: "????????????",
  ADD_ARTICLE: "????????????",
  EDIT_ARTICLE: "????????????",

  ORG_DETAIL: "??????????????????",
  ORG_PRINCIPAL: "??????????????????(??????????????????????????????)",
  ORG_LIST: "??????????????????",
  ORG_DISPLAY_NAME: "??????????????????",
  ADD_ORG: "??????????????????",
  EDIT_ORG: "??????????????????",

  SKU_LIST: "SKU??????",
  ADD_SKU: "??????SKU",
  EDIT_SKU: "??????SKU",

  SYMBOL_ADD: "+",
  SYMBOL_CROSS: "x",

  SAVE: "??????",
  SAVED: "?????????",
  DELETE: "??????",

  CANCEL: "??????",
  SUBMIT: "??????",

  LOGIN: "??????",
  LOGOUT: "??????",

  PREVIEW: "??????",
  SUSPEND: "??????",
  CONFIRM: "??????",

  OOPS: "?????????",

  CREATED: "??????",
  PENDING: "?????????",
  APPROVED: "?????????",
  DENIED: "?????????",
  ADMIN_SUSPENDED: "?????????????????????",
  AUTHOR_SUSPENDED: "??????????????????",

  LATEST: "??????",
  MISC: "??????",

  VIEW_AS_PLAYER: "??????????????????",
  PLEASE_INPUT_KEYWORD: "????????????",

  ARTICLE_PROVIDED_BY_PREFIX: "?????????",
  ARTICLE_PROVIDED_BY_SUFFIX: "??????",

  INPUT_REASON: "????????????",
  SUSPENSION_REASON: "????????????",

  APP_NAME: "Demo",
  ABSTACT: "Internal Training Project",

  WRITER_PRINCIPAL_DUPLICATED: "????????????????????????",
  ORG_OR_WRITER_PRINCIPAL_DUPLICATED: "?????????????????????????????????????????????",

  LIVECHAT_CONSOLE: "?????????????????????",

  CURRENCY_DICT: {},

  MODERATOR: "?????????",

  HINT: {
    IMAGE_REQUIREMENT: "???????????????????????????",
    VIDEO_REQUIREMENT: "???????????????????????????",
  }
};

ZH_CN.CURRENCY_DICT[CURRENCY.RMB] = {
  UNIT: "??????????????????",
  VALUE_PLACEHOLDER: "???????????????????????????\"???\"?????????",
};

exports.LANG = {
  ZH_CN: ZH_CN,
};

exports.DESC = "DESC";
exports.ASC = "ASC";
exports.DIRECTION_FORWARD = 1;
exports.DIRECTION_BACKWARD = -1;

exports.CREATED_AT = "created_at";
exports.UPDATED_AT = "updated_at";

exports.WECHAT_JSAPI_TICKET = "wechatJsapiTicket";
exports.WECHAT_JSAPI_ACCESS_TOKEN = "wechatJsapiAccessToken";

exports.HTTP_STATUS_CODE = {
  UNWRITERIZED: 401,
}

exports.THEME = {
  MAIN: {
    WHITE: "white",
    BLACK: "black",
    GREY: "grey",
    RED: "red",
    CRIMSON: "crimson",
    BROWN_REDISH: "rgba(128, 64, 0, 0.5)",
    BLUE: "blue",
    TRANSPARENT: "transparent",
    KEYWORD: "#333333",
    KEYWORD_BG: "#FCE1A4",
  },
};

exports.ROLE_NAME = {
  ADMIN: "admin",
  WRITER: "writer",
  PLAYER: "player",
};

exports.WEB_FRONTEND_COOKIE_INT_AUTH_TOKEN_KEY = "IntAuthToken";
exports.WEB_FRONTEND_LOCAL_STORAGE_LOGGED_IN_ROLE_KEY = "LoggedInRole";

exports.ORG = {
  LIST_ITEM_ENTITY_TYPE_LITERAL: {
    SUBORG: "suborg", 
    WRITER: "writer",
  },
};

exports.SUBORG = {
  TYPE: {
    MODERATOR: 0,
    EMPLOYEE: 1,
  },
};

exports.ATTACHMENT = {
  ORIGINAL: {
    LITERAL: "original",
  },
  TRANSCODED_PREFIX: {
    LITERAL: "transcoded_",
  },
  STATE: {
    /*
    * For each "attachment" record, it could be "deleted softly" during any state, including "SOLIDIFIED_PENDING_TRANSCODING".
    *
    * -- YFLu, 2020-03-03
    */

    /* Of the original file. */
    CREATED_UNSOLIDIFIED: 0,
    SOLIDIFIED: 1,
    SOLIDIFIED_TRANSCODING_COPIES: 2,
    TRANSCODED_COPIES_ALL_COMPLETED: 3,
    TRANSCODED_COPIES_PARTIALLY_COMPLETED: 4,
    TRANSCODED_COPIES_ALL_FAILED: 5,

    /* Of each transcoding/transcoded copy. */
    CREATED_TRANSCODING_COPY_UNSOLIDIFIED: 50,
    TRANSCODED_COPY_SUCCESSFUL: 51,
    TRANSCODED_COPY_ERROR_TIMED_OUT: 52,
    TRANSCODED_COPY_ERROR_PAYLOAD: 53,
  },

  META_TYPE: {
    UNKNOWN: 0,
    ARTICLE: 1,
  },

  OWNER_META_TYPE: {
    UNKNOWN: 0,
    WRITER: 1,
  },

  IMAGE: {
    LITERAL: "image",
    POLICY: {
      N_PER_ARTICLE: 3,
      SINGLE_SIZE_LIMIT_BYTES: (1 << 21), // 2 MB
      WRITE_ALLOWED_MIME_TYPES: [
        "image/jpeg",
        "image/png",
      ],
      READ_ALLOWED_MIME_TYPES: [
        "image/jpeg",
        "image/png",
        "image/gif",
      ],
    },
  },

  VIDEO: {
    LITERAL: "video",
    POLICY: {
      N_PER_ARTICLE: 1,
      SINGLE_SIZE_LIMIT_BYTES: (1 << 29), // 512 MB
      WRITE_ALLOWED_MIME_TYPES: [
        "video/webm", // .webm
        "video/mp4", // .mp4
        "video/quicktime", // .mov 
        "video/x-msvideo", // .avi 
        "video/x-ms-wmv", // .wmv 
      ],
      READ_ALLOWED_MIME_TYPES: [
        "video/webm", // .webm
        "video/mp4", // .mp4
        "video/quicktime", // .mov 
        "video/x-msvideo", // .avi 
        "video/x-ms-wmv", // .wmv 

        "video/x-flv", // .flv

        // HLS mimetype Reference, https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/StreamingMediaGuide/DeployingHTTPLiveStreaming/DeployingHTTPLiveStreaming.html
        "application/x-mpegURL", // .m3u8 
        "video/MP2T", // .ts 
      ],
    },
  },
};

exports.OSS_DELETION_RESULT_CODE = {
  UNKNOWN: 0,
  DELETED: 1,
  NOT_DELETED_NO_LONGER_EXISTING: 2,
  NOT_DELETED_PERMISSION_DENIED: 3,
};

exports.KEY = "key";

exports.YAMD = {
  TAG: {
    IMAGE: 'iimag',
    VIDEO: 'pvd',
    KATEX: 'katex',
    MERMAID: 'mermaid',
    ALIGN_CENTER: 'algctr',
  }
};

// Use "CommonJs `require`" syntax to import for both NodeJsBackend and React16Frontend to guarantee compatibility.
