var map = null;
$(function() {
	initMap();
	var geoc = new BMap.Geocoder();    
	map.addEventListener("click",function(e){
		remove_overlay();
		addPoint(e.point);
		var index = parent.layer.getFrameIndex(window.name);
		geoc.getLocation(e.point, function(rs){
			parent.locate({
				location: e.point.lat + "," + e.point.lng,
				address: rs.address
			});
			parent.layer.close(index);
		});   
	});
});
function initMap(){
	map = new BMap.Map("allmap");
	var center = new BMap.Point(116.404, 39.915);
	if(!Utils.isEmpty(_location)){
		var ary = _location.split(",");
		if(ary.length == 2){
			center = new BMap.Point(ary[1], ary[0]);
			addPoint(center);
			map.centerAndZoom(center, 15);
		}
	}else
		map.centerAndZoom(center, 10); 
	// 左上角，添加比例尺
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
	var top_left_navigation = new BMap.NavigationControl(); 
	//添加控件和比例尺
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation); 
	map.enableScrollWheelZoom(true);
}

function addPoint(point){
	var marker = new BMap.Marker(point); 
	map.addOverlay(marker); 
	marker.setAnimation(BMAP_ANIMATION_BOUNCE);
}
function remove_overlay(){
	map.clearOverlays();         
}


function searchMap() {
	var area = document.getElementById("input").value;
	var ls = new BMap.LocalSearch(map);
	ls.setSearchCompleteCallback(function(rs) {
		if (ls.getStatus() == BMAP_STATUS_SUCCESS) {
			var poi = rs.getPoi(0);
			if (poi) {
				_location=poi.point.lat+","+poi.point.lng ;
				initMap();
			}
		}
	})	;
	ls.search(area);
}



