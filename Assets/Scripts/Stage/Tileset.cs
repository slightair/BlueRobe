using System;
using System.Collections;
using System.Collections.Generic;
using System.Xml;

namespace BlueRobe.Stage
{
    public class TileSet
    {
        public string name;
        public int tileWidth;
        public int tileHeight;
        public List<Tile> tiles;

        public TileSet(XmlNode node)
        {
            ParseAttributes(node);
            ParseTiles(node);
        }

        private void ParseAttributes(XmlNode node)
        {
            name = node.Attributes["name"].Value;
            tileWidth = int.Parse(node.Attributes["tilewidth"].Value);
            tileHeight = int.Parse(node.Attributes["tileheight"].Value);
        }

        private void ParseTiles(XmlNode node)
        {
            UInt32 gid = UInt32.Parse(node.Attributes["firstgid"].Value);

            tiles = new List<Tile>();
            XmlNodeList tileNodes = node.SelectNodes("tile");
            IEnumerator enumerator = tileNodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                XmlNode tileNode = (XmlNode)enumerator.Current;
                tiles.Add(new Tile(gid, tileNode));
                gid++;
            }
        }
    }
}