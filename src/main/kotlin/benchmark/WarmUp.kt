package benchmark

object WarmUp {

    class Dummy {
        fun doNothing() {

        }
    }

    fun run() {
        for (i in 0..1_000_000) {
            val dummy = Dummy()
            dummy.doNothing()
        }
    }

}