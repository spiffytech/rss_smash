/*
 * Preserve enclosures in RSS feeds
 */
module.exports = function (item, itemOptions, source) {
    if (item.enclosures && Array.isArray(item.enclosures)){
        item.enclosures.forEach(function(enclosure){
            var element = {
                enclosure :{
                    _attr : {
                        url     : enclosure.url,
                        length  : enclosure.length || enclosure.size || 0,
                        type    : enclosure.type || mime.lookup(enclosure.url)
                    }
                }
            };

            itemOptions.custom_elements.push(element);
        });
    }
    return itemOptions;
};
