var RssBraider = require("rss-braider");
var feeds = {};

// Pull feeds from config files:
//      feeds.simple_test_feed = require("./config/feed").feed;
// Or define in-line
feeds.tv_torrents = {
    "feed_name"             : "feed",
    "default_count"         : 50,
    "no_cdata_fields"       : [], // Don"t wrap these fields in CDATA tags
    "plugins"               : ["preserve_enclosures"],
    "meta" : {
        "title": "spiffytech's combined rss feed",
        "description": "A combined RSS feed"
    },
    "sources" : [
        {
            "name"              : "spiffytech's feeds",
            "feed_url"          : "http://spiffy.tech/tv_torrents.raw.rss",
        },
        {
            "name"              : "ShowRSS",
            //"feed_url"          : "http://showrss.info/rss.php?user_id=248483&hd=null&proper=null"
            "feed_url"          : "http://showrss.info/user/25155.rss?magnets=true&namespaces=true&name=clean&quality=hd&re=yes"
        }
    ]
};
var braider_options = {
    feeds           : feeds,
    indent          : "    ",
    date_sort_order : "desc", // Newest first
    log_level       : "off",
    plugins_directories : [__dirname + "/plugins/"]
};
var rss_braider = RssBraider.createClient(braider_options);

// Override logging level (debug, info, warn, err, off)
rss_braider.logger.level("off");

// Output braided feed as rss. use "json" for JSON output.
rss_braider.processFeed("tv_torrents", "rss", function(err, data){
    if (err) {
        return console.log(err);
    } else {
        console.log(data);
    }
});
