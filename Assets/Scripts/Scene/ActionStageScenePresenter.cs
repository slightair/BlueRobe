using UnityEngine;
using UniRx;

namespace BlueRobe.Scene
{
    public class ActionStageScenePresenter : MonoBehaviour
    {
        public Character character;

        void Start()
        {
            character.IsFinished.Where(f => f)
                                .Subscribe(_ =>
                                {
                                    character.StopMoving();
                                    Invoke("switchTitleScene", 3);
                                });
            Invoke("startGame", 3);
        }

        private void startGame()
        {
            character.StartMoving();
        }

        private void switchTitleScene()
        {
            SceneManager.Instance.SwitchScene("Title");
        }
    }
}