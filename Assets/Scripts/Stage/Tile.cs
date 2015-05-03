using System;
using System.Xml;
using System.Collections;

namespace BlueRobe.Stage
{
    public class Tile
    {
        public UInt32 globalId;
        public int id;
        public string name;
        public int imageWidth;
        public int imageHeight;
        public string imageSource;

        public Tile(UInt32 gid, XmlNode node)
        {
            globalId = gid;
            id = int.Parse(node.Attributes["id"].Value);

            ParseProperties(node);

            XmlNode imageNode = node.SelectSingleNode("image");
            imageWidth = int.Parse(imageNode.Attributes["width"].Value);
            imageHeight = int.Parse(imageNode.Attributes["height"].Value);
            imageSource = imageNode.Attributes["source"].Value;
        }

        private void ParseProperties(XmlNode node)
        {
            XmlNodeList propertyNodes = node.SelectNodes("properties/property");
            IEnumerator enumerator = propertyNodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                XmlNode propertyNode = (XmlNode)enumerator.Current;
                if (propertyNode.Attributes["name"].Value.Equals("name"))
                {
                    name = propertyNode.Attributes["value"].Value;
                }
            }
        }

        override public string ToString()
        {
            return string.Format("id:{0}, name:{1}, {{{2}, {3}}} ({4})", id, name, imageWidth, imageHeight, imageSource);
        }
    }
}