using System;
using System.Collections;
using System.Collections.Generic;
using System.Xml;

namespace BlueRobe.Stage
{
    public class Map
    {
        public enum GroundType
        {
            Grass
        };

        public int width;
        public int height;
        public int tileWidth;
        public int tileHeight;
        public GroundType groundType;
        public Dictionary<string, TileSet> tileSets;
        public Dictionary<string, Layer> layers;

        public Map(XmlElement element)
        {
            ParseAttributes(element);
            ParseProperties(element);
            ParseTileSets(element);
            ParseLayers(element);
        }

        public Tile GetTile(string layerName, int x, int y)
        {
            Layer layer = layers[layerName];
            if (layer == null)
            {
                return null;
            }

            int index = layer.width * (layer.height - y - 1) + x;
            UInt32 tileId = layer.tileIdList[index];

            Tile tile = null;
            IEnumerator enumerator = tileSets.Values.GetEnumerator();
            while (enumerator.MoveNext())
            {
                TileSet tileSet = (TileSet)enumerator.Current;
                tile = tileSet.tiles.Find(t => t.globalId == tileId);

                if (tile != null)
                {
                    break;
                }
            }
            return tile;
        }

        private void ParseAttributes(XmlElement element)
        {
            width = int.Parse(element.Attributes["width"].Value);
            height = int.Parse(element.Attributes["height"].Value);
            tileWidth = int.Parse(element.Attributes["tilewidth"].Value);
            tileHeight = int.Parse(element.Attributes["tileheight"].Value);
        }

        private void ParseProperties(XmlElement element)
        {
            XmlNodeList propertyNodes = element.SelectNodes("properties/property");
            IEnumerator enumerator = propertyNodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                XmlNode node = (XmlNode)enumerator.Current;
                if (node.Attributes["name"].Value.Equals("baseGroundType"))
                {
                    groundType = (GroundType)Enum.Parse(typeof(GroundType), node.Attributes["value"].Value);
                }
            }
        }

        private void ParseTileSets(XmlElement element)
        {
            tileSets = new Dictionary<string, TileSet>();
            XmlNodeList tilesetNodes = element.SelectNodes("tileset");
            IEnumerator enumerator = tilesetNodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                XmlNode node = (XmlNode)enumerator.Current;
                TileSet tileSet = new TileSet(node);
                tileSets.Add(tileSet.name, tileSet);
            }
        }

        private void ParseLayers(XmlElement element)
        {
            layers = new Dictionary<string, Layer>();
            XmlNodeList layerNodes = element.SelectNodes("layer");
            IEnumerator enumerator = layerNodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                XmlNode node = (XmlNode)enumerator.Current;
                Layer layer = new Layer(node);
                layers.Add(layer.name, layer);
            }
        }
    }
}