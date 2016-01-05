<html>
<head>
    <title>Openplanning mit Geoserver</title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
		<script src="<?php echo base_url()?>openlayers/OpenLayers2.js" type="text/javascript"></script>
		<script src="<?php echo base_url()?>openlayers/build/ol.js" type="text/javascript"></script>
		<script src="http://maps.google.com/maps/api/js?v=3.5&amp;sensor=false"></script>
	<script>
		function init(){
			var layers = [
				new ol.layer.Tile({
					source: new ol.source.MapQuest({layer: 'sat'})
				}),
				new ol.layer.Tile({
					extent: [11880112,-731903.625,11881295,-731519.1875],
					source: new ol.source.TileWMS(/** @type {olx.source.TileWMSOptions} */ ({
						url: 'http://localhost:8080/geoserver/ipb_gis/wms',
						params: {'LAYERS': 'ipb_gis:level4', 'TILED': true},
						serverType: 'geoserver'
					}))
				})
			];
			
			var map = new ol.Map({
				layers: layers,
				target: 'map',
				view: new ol.View({
					center: [106.7258435,-6.5584798],
					zoom: 20
				})
			});
		}
	</script>
</head>

 <body onload="init()">
    <div id="map"></div>
    </div>
 </body>
 </html>