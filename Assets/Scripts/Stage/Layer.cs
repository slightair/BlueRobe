using System;
using System.Xml;
using System.Linq;
using System.Collections.Generic;
using System.IO;

namespace BlueRobe.Stage
{
    public class Layer
    {
        public string name;
        public int width;
        public int height;
        public List<UInt32> tileIdList;

        public Layer(XmlNode node)
        {
            name = node.Attributes["name"].Value;
            width = int.Parse(node.Attributes["width"].Value);
            height = int.Parse(node.Attributes["height"].Value);

            ParseData(node);
        }

        private void ParseData(XmlNode node)
        {
            // assume CSV
            string dataString = node.SelectSingleNode("data").InnerText;
            tileIdList = new List<UInt32>(dataString.Split(',')
                .Select(t => UInt32.Parse(t))
                .ToList());
            tileIdList.AsReadOnly();
        }

        override public string ToString()
        {
            StringWriter writer = new StringWriter();
            for (int y = 0; y < height; y++)
            {
                List<UInt32> lines = tileIdList.GetRange(y * width, width);
                writer.WriteLine(string.Join("", lines.Select(i => i.ToString()).ToArray()));
            }
            return writer.ToString();
        }
    }
}