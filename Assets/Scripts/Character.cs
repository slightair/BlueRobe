using UnityEngine;
using UniRx;

public class Character : MonoBehaviour
{
    private static float MoveRatio = 0.05f;
    private static float MoveMax = 0.8f;
    private static float JumpPower = 200f;

    public bool IsDead { get; private set; }
    public ReactiveProperty<bool> IsFinished { get; private set; }

    private Rigidbody rigidBody;
    private Animator animator;
    private float forwardSpeed;
    private bool isGrounded;
    private bool isWallTouched;
    private bool isJumpCancelled;
    private Vector3 move;

    void Start()
    {
        rigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();

        IsFinished = new ReactiveProperty<bool>(false);
    }

    void FixedUpdate()
    {
        move = transform.position;
        move += transform.forward * forwardSpeed;

        ProcessTouches();
        ProcessKeys();

        rigidBody.MovePosition(move);
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("Ground"))
        {
            isGrounded = true;
            animator.SetBool("isGrounded", true);
        }
        else if (collision.gameObject.CompareTag("Wall"))
        {
            isWallTouched = true;
        }
    }

    void OnCollisionExit(Collision collision)
    {
        if (collision.gameObject.CompareTag("Ground"))
        {
            isGrounded = false;
            animator.SetBool("isGrounded", false);
        }
        else if (collision.gameObject.CompareTag("Wall"))
        {
            isWallTouched = false;
        }
    }

    void OnTriggerEnter(Collider collider)
    {
        if (collider.gameObject.CompareTag("Finish"))
        {
            IsFinished.Value = true;
        }
        else if (collider.gameObject.CompareTag("Over"))
        {
            IsDead = true;
            IsFinished.Value = true;
        }
    }

    public void StartMoving()
    {
        forwardSpeed = 0.5f;
        animator.SetBool("isRunning", true);

        GameObject plane = GameObject.Find("Plane");
        GameObject runDust = GameObject.Find("RunDust");
        Renderer planeRenderer = plane.GetComponent("Renderer") as Renderer;
        Renderer dustRenderer = runDust.GetComponent("Renderer") as Renderer;
        dustRenderer.material.color = planeRenderer.material.color;

        ParticleSystem runDustParticleSystem = runDust.GetComponent("ParticleSystem") as ParticleSystem;
        runDustParticleSystem.Play();
    }

    public void StopMoving()
    {
        forwardSpeed = 0f;
        animator.SetBool("isRunning", false);

        GameObject runDust = GameObject.Find("RunDust");
        ParticleSystem runDustParticleSystem = runDust.GetComponent("ParticleSystem") as ParticleSystem;
        runDustParticleSystem.Stop();
    }

    private void ProcessTouches()
    {
        if (Input.touchCount == 0)
        {
            return;
        }

        Touch touch = Input.GetTouch(0);
        if (touch.phase == TouchPhase.Began)
        {
            PrepareJump();
        }
        else if (touch.phase == TouchPhase.Moved)
        {
            Move(touch.deltaPosition.x);
        }
        else if (touch.phase == TouchPhase.Ended || touch.phase == TouchPhase.Canceled)
        {
            Jump();
        }
    }

    private void ProcessKeys()
    {
        if (Input.GetKeyDown("space"))
        {
            PrepareJump();
        }
        else if (Input.GetKeyUp("space"))
        {
            Jump();
        }

        float deltaX = Input.GetAxisRaw("Horizontal") * 5;
        if (deltaX != 0)
        {
            Move(deltaX);
        }
    }

    private void PrepareJump()
    {
        animator.SetBool("PrepareJump", true);
        isJumpCancelled = false;
    }

    private void Move(float deltaX)
    {
        if (isWallTouched && transform.position.z * deltaX > 0)
        {
            return;
        }

        isJumpCancelled = true;
        animator.SetBool("PrepareJump", false);
        AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);
        if (deltaX != 0)
        {
            move += transform.right * Mathf.Clamp(deltaX * MoveRatio, -MoveMax, MoveMax);
        }
    }

    private void Jump()
    {
        animator.SetBool("PrepareJump", false);
        if (isGrounded && !isJumpCancelled)
        {
            rigidBody.AddForce(Vector3.up * JumpPower, ForceMode.Impulse);
        }
    }
}
