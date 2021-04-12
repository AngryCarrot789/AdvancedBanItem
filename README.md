# AdvancedBanItem
a plugin for banning or limiting block placement/breaking/interacting for minecraft. i made this for 1.6.4 so it should work on versions around there

uses hash maps :)
item id + (metadata bitshifted right by 16) makes a hashcode used by a hashmap for fetching block limiters which contain a hashmap of metalimits key'd to the metadata 
