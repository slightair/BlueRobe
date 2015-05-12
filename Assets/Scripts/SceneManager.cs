using UnityEngine;
using BlueRobe.Utility;

namespace BlueRobe
{
    public class SceneManager : SingletonMonoBehaviour<SceneManager>
    {
        public void SwitchScene(string sceneName)
        {
            Application.LoadLevel(sceneName);
        }
    }
}