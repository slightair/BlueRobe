using UnityEngine;

namespace BlueRobe.Scene
{
    public class TitleScenePresenter : MonoBehaviour
    {
        void Start()
        {

        }

        void Update()
        {

        }

        public void OnTapToStartButton()
        {
            StartGame();
        }

        private void StartGame()
        {
            SceneManager.Instance.SwitchScene("ActionStage");
        }
    }
}
