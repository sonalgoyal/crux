require("../env");
require("../../d3");
require("../../d3.geo");

var vows = require("vows"),
    assert = require("assert");

var suite = vows.describe("d3.geo.azimuthal");

suite.addBatch({
  "azimuthal.stereographic": {
    topic: function() {
      return d3.geo.azimuthal().mode("stereographic").translate([0, 0]).scale(100);
    },
    "Arctic": function(azimuthal) {
      var coords = azimuthal([0, 85]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], -91.633117, 1e-6);
      var lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], 85, 1e-6);
    },
    "Antarctic": function(azimuthal) {
      var coords = azimuthal([0, -85]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], 91.633117, 1e-6);
      var lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], -85, 1e-6);
    },
    "Hawaii": function(azimuthal) {
      var coords = azimuthal([-180, 0]);
      assert.equal(coords[0], -Infinity);
      assert.isTrue(isNaN(coords[1]));
    },
    "Phillipines": function(azimuthal) {
      var coords = azimuthal([180, 0]);
      assert.equal(coords[0], Infinity);
      assert.isTrue(isNaN(coords[1]));
    },
    "Inversion works for non-zero translation": function() {
      var azimuthal = d3.geo.azimuthal().mode("stereographic").translate([123, 99]).scale(100),
          coords = azimuthal([0, 85]),
          lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], 85, 1e-6);
    }
  },
  "azimuthal.orthographic": {
    topic: function() {
      return d3.geo.azimuthal().mode("orthographic").translate([0, 0]).scale(100);
    },
    "Arctic": function(azimuthal) {
      var coords = azimuthal([0, 85]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], -99.619469, 1e-6);
      var lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], 85, 1e-6);
    },
    "Antarctic": function(azimuthal) {
      var coords = azimuthal([0, -85]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], 99.619469, 1e-6);
      var lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], -85, 1e-6);
    },
    "Hawaii": function(azimuthal) {
      var coords = azimuthal([-180, 0]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], 0, 1e-6);
    },
    "Phillipines": function(azimuthal) {
      var coords = azimuthal([180, 0]);
      assert.inDelta(coords[0], 0, 1e-6);
      assert.inDelta(coords[1], 0, 1e-6);
    },
    "Inversion works for non-zero translation": function() {
      var azimuthal = d3.geo.azimuthal().mode("orthographic").translate([123, 99]).scale(100),
          coords = azimuthal([0, 85]),
          lonlat = azimuthal.invert(coords);
      assert.inDelta(lonlat[0], 0, 1e-6);
      assert.inDelta(lonlat[1], 85, 1e-6);
    }
  }
});

suite.export(module);
